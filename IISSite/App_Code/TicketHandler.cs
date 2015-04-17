using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net;

/// <summary>
/// Summary description for SessionHandler
/// </summary>
public class TicketHandler : IHttpHandler
{
    private static Dictionary<string, DateTime> _Tickets = new Dictionary<string, DateTime>();
    
    public static string TicketVariableName
    {
        get { return System.Configuration.ConfigurationManager.AppSettings["TicketVariableName"]; }
    }

    public static TimeSpan TicketLifetime
    {
        get { return TimeSpan.Parse(System.Configuration.ConfigurationManager.AppSettings["TicketLifetime"]); }
    }

        public bool IsReusable
    {
        get { return true; }
    }

    public void ProcessRequest(HttpContext context)
    {
        var winAuthSessionId = context.Request[TicketVariableName];
        if (!string.IsNullOrEmpty(winAuthSessionId))
        {
            Cleanup();
            if (UseTicket(winAuthSessionId))
            {
                context.Response.StatusCode = (int)HttpStatusCode.OK;
                context.Response.Write("OK");
                context.Response.Flush();
                return;
            }
        }
        context.Response.StatusCode = (int)HttpStatusCode.BadRequest;
    }

    
    public static string IssueTicket()
    {
        var ret = Guid.NewGuid().ToString();
        lock (_Tickets)
            _Tickets[ret] = DateTime.Now;
        return ret;
    }

    public void Cleanup()
    {
        var oldEntries = _Tickets.Where(kvp => kvp.Value < DateTime.Now.AddMilliseconds(-Math.Abs(TicketLifetime.TotalMilliseconds))).ToArray();
        lock (_Tickets)
        {
            foreach (var kvp in oldEntries)
                if (_Tickets.ContainsKey(kvp.Key))
                    _Tickets.Remove(kvp.Key);
        }
    }

    public static bool UseTicket(string ticket)
    {
        lock(_Tickets)
        {
            if (_Tickets.ContainsKey(ticket))
            {
                var timestamp = _Tickets[ticket];
                //_Tickets.Remove(ticket);
                return true;
            }
        }
        return false;
    }
}