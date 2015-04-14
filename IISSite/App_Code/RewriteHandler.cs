using System;
using System.Web;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;
using System.Linq;

public class RewriteHandler : IHttpHandler
{
    /// <summary>
    /// You will need to configure this handler in the web.config file of your 
    /// web and register it with IIS before being able to use it. For more information
    /// see the following link: http://go.microsoft.com/?linkid=8101007
    /// </summary>


    public bool IsReusable
    {
        // Return false in case your Managed Handler cannot be reused for another request.
        // Usually this would be false in case you have some state information preserved per request.
        get { return true; }
    }

    public void ProcessRequest(HttpContext context)
    {
        var url = System.Configuration.ConfigurationManager.AppSettings["RewriteUrl"]
            //+ context.Request.Path;
            + context.Request.ServerVariables["HTTP_URL"];

        var uri = HackedUri.Create(url);
        System.Net.HttpWebRequest req = System.Net.WebRequest.Create(uri) as HttpWebRequest;
        req.Method = context.Request.HttpMethod;
        req.AllowAutoRedirect = false; // Send redirect bach to browser           

        CopyRequestHeaders(context, req);
        AddAuthenticationCookies(context, req);
        CopyRequestStream(context, req);

        // Get response
        var res = req.GetResponse() as HttpWebResponse;

        if (res.StatusCode == HttpStatusCode.SeeOther)
        {
            context.Response.Redirect(res.Headers["Location"]);
            context.Response.End();
            return;
        }
        CopyResponseHeaders(context, res);

        CopyResponseStream(context, res);
    }

    private static void CopyRequestHeaders(HttpContext context, System.Net.HttpWebRequest req)
    {
        // Copy request headers
        foreach (var headerKey in context.Request.Headers.AllKeys)
        {
            var val = context.Request.Headers[headerKey];
            switch (headerKey.ToLower())
            {
                case "connection":
                    if (val.ToLower().Equals("keep-alive"))
                        req.KeepAlive = true;
                    else if (val.ToLower().Equals("close"))
                        req.KeepAlive = false;
                    else
                        req.Connection = val;
                    break;

                case "accept":
                    req.Accept = val;
                    break;

                case "host":
                    req.Host = val;
                    break;

                case "user-agent":
                    req.UserAgent = val;
                    break;

                case "content-length":
                    req.ContentLength = int.Parse(val);
                    break;

                case "content-type":
                    req.ContentType = val;
                    break;

                case "referer":
                    req.Referer = val;
                    break;

                default:
                    req.Headers[headerKey] = val;
                    break;
            }
        }
    }

    private static void AddAuthenticationCookies(HttpContext context, System.Net.HttpWebRequest req)
    {
        // Copy user identity to new cookie
        if (context.User != null && context.User.Identity != null)
        {
            var cookiesString0 = req.Headers[HttpRequestHeader.Cookie];
            var cookies = CookieParser.Parse(cookiesString0).ToList();

            // Pass username
            var userCookie = new Cookie("") { Name = "username", Value = context.User.Identity.Name };
            cookies.Add(userCookie);// Always add because this 

            // Pass user groups
            var windowsIdentity = context.User.Identity as System.Security.Principal.WindowsIdentity;
            var groupNames = new string[] { };
            if (windowsIdentity.Groups != null)
                groupNames = windowsIdentity.Groups.Select(g => g.Translate(typeof(System.Security.Principal.NTAccount)).Value).ToArray();
            var groupsCookie = new Cookie("") { Name = "usergroups", Value = string.Join(",", groupNames) };
            cookies.Add(groupsCookie);

            // Authentication ticket
            var ticket = TicketHandler.IssueTicket();
            var ticketCookie = new Cookie("") { Name = "winauthticket", Value = ticket };
            cookies.Add(ticketCookie);

            var cookiesString = CookieParser.ToString(cookies.ToArray());
            req.Headers[HttpRequestHeader.Cookie] = cookiesString;
        }
    }

    private static void CopyRequestStream(HttpContext context, System.Net.HttpWebRequest req)
    {
        // Copy request content
        if (req.ContentLength > 0)
        {
            using (var reqStream = req.GetRequestStream())
            {
                var reqBytes = new byte[context.Request.InputStream.Length];
                context.Request.InputStream.Read(reqBytes, 0, reqBytes.Length);
                reqStream.Write(reqBytes, 0, reqBytes.Length);
            }
        }
    }

    private static void CopyResponseHeaders(HttpContext context, HttpWebResponse res)
    {
        // Copy responbse headers
        foreach (var headerKey in res.Headers.AllKeys)
        {
            var val = res.Headers[headerKey];
            switch (headerKey.ToLower())
            {
                case "content-length":
                    break;

                case "content-type":
                    context.Response.ContentType = val;
                    break;

                case "location":
                    break;

                case "cache-control":
                    break;

                case "date":
                    break;
                case "etag":
                    break;
                case "last-modified":
                    break;

                case "set-cookie":
                    foreach (var cookieString in val.Split(new char[] { ';' }, StringSplitOptions.RemoveEmptyEntries))
                    {
                        var cookie = new Cookie(cookieString);
                        context.Response.Cookies.Add(new HttpCookie(cookie.Name, cookie.Value));
                    }
                    break;

                default:
                    try
                    {
                        context.Response.Headers.Add(headerKey, val);
                    }
                    catch (Exception ex)
                    {
                        throw ex;
                    }
                    break;
            }
        }
    }

    private static void CopyResponseStream(HttpContext context, HttpWebResponse res)
    {
        //Copy response data or redirect
        context.Response.StatusCode = (int)res.StatusCode;
        using (var resStream = res.GetResponseStream())
        {
            if (string.IsNullOrEmpty(res.CharacterSet)) // Binary response (image)
            {
                var resBytes = new byte[res.ContentLength];
                resStream.Read(resBytes, 0, resBytes.Length);
                context.Response.OutputStream.Write(resBytes, 0, resBytes.Length);
            }
            else
            {
                var enc = Encoding.GetEncoding(res.CharacterSet); // text response
                using (var rd = new System.IO.StreamReader(resStream))
                {
                    var resString = rd.ReadToEnd();
                    context.Response.Output.Write(resString);
                }
            }
        }
    }
}
