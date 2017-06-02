using System;
using System.Net;
using System.Web;
using System.Collections.Generic;
using System.Linq;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace util
{

    // TODO : Test AccessLevelManager class after C# conversion
    /**
     * Created by Beemen on 23/03/2015.
     */
    public abstract class AccessLevelManager
    {
        public static bool getCurrentUseCart()
        {
            updateAccessLevel(HttpContext.Current.User.Identity.Name);

            // Now we are sure the value is stored
            return (bool)HttpContext.Current.Session["usecart"];
        }

        public static int getCurrentAccessLevel()
        {
            updateAccessLevel(HttpContext.Current.User.Identity.Name);

            // Now we are sure the value is stored
            return (int)HttpContext.Current.Session["accesslevel"];
        }

        public static void updateAccessLevel(String username)
        {
            HttpContext.Current.Session.Remove("accesslevel"); // invaidate current access level to force re-check

            var windowsIdentity = HttpContext.Current.User.Identity as System.Security.Principal.WindowsIdentity;
            var groupNames = new string[] { };
            if (windowsIdentity.Groups != null)
                groupNames = windowsIdentity.Groups
                    .Where(g => g.Translate(typeof(System.Security.Principal.NTAccount)) != null)
                    .Select(g => g.Translate(typeof(System.Security.Principal.NTAccount)).Value).ToArray();

            bool useCart;
            int accessLevel = calculateAccessLevel(username, groupNames, out useCart);

            HttpContext.Current.Session["accesslevel"] = accessLevel; // good for 2 hours
            HttpContext.Current.Session["usecart"] = useCart;

            play.Logger.info("ACCESS LEVEL UPDATED TO " + HttpContext.Current.Session["accesslevel"]);
        }

        private static int calculateAccessLevel(String username, String[] groupnames, out bool useCart)
        {
            // parse csv access levels file
            var config = System.Web.Configuration.WebConfigurationManager.OpenWebConfiguration("~");
            var mainConfigPath = config.FilePath;
            String accessFileName = Path.Combine(
                new FileInfo(mainConfigPath).Directory.FullName,
                string.Format("{0}", cpreader.Properties.Settings.Default.accessfile_name)
                );
            String accessFileURL = cpreader.Properties.Settings.Default.accessfile_url + accessFileName;
            

            // Initial values
            var accesslevel = 0;
            var DELIMITER = ',';

            bool onlineFile = cpreader.Properties.Settings.Default.accessfile_online;

            var linesText = "";
            try
            {
                linesText = onlineFile ? Encoding.UTF8.GetString(new WebClient().DownloadData(cpreader.Properties.Settings.Default.accessfile_url))
                    : System.IO.File.ReadAllText(accessFileName);
            }
            catch (Exception e)
            {
                play.Logger.info("ERROR ");
            }

            int dummyInt;
            var entries = linesText
                .Split(Environment.NewLine.ToArray(), StringSplitOptions.RemoveEmptyEntries)
                .Where(line => !string.IsNullOrEmpty(line) && !line.Trim().StartsWith("#"))
                .Select(line => line.Split(DELIMITER).Select(t => t.Trim()).ToArray())
                .Select(tokens => new
                {
                    Name = tokens.First().ToLower().Replace(" ", ""),
                    AccessLevel = (tokens.Length > 1 && int.TryParse(tokens[1], out dummyInt)) ? Convert.ToInt32(tokens[1]) : null as int?,
                    UseCart = (tokens.Length > 2 && int.TryParse(tokens[2], out dummyInt)) ? Convert.ToBoolean(Convert.ToInt32(tokens[2])) : null as bool?
                });


            var searchNames = groupnames.Union(new string[] { username }).ToArray();

            var matchedLine = entries.FirstOrDefault(
                l => searchNames.Contains(l.Name) || l.Name == "*");


            if (matchedLine != null)
            {
                if (matchedLine.AccessLevel.HasValue)
                    accesslevel = matchedLine.AccessLevel.Value;
                else
                    accesslevel = 0;

                if (matchedLine.UseCart != null)
                    useCart = matchedLine.UseCart.Value && cpreader.Properties.Settings.Default.cart_enabled;
                else
                    useCart = cpreader.Properties.Settings.Default.cart_enabled;
            }
            else
            {
                accesslevel = 0;
                useCart = cpreader.Properties.Settings.Default.cart_enabled;
            }

            return accesslevel; // lowest level access as default
        }
    }
}