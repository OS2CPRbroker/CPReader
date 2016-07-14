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
            var ret = HttpContext.Current.Session["usecart"];
            if (ret == null)
                getCurrentAccessLevel();
            return HttpContext.Current.Session["usecart"] as string == "1";
        }

        public static int getCurrentAccessLevel()
        {
            //if (Cache.get("accesslevel") == null)
            // Always check from current authentication
            //if (Context.current().session().get("accesslevel") == null)
            {
                String userName = HttpContext.Current.User.Identity.Name;

                var windowsIdentity = HttpContext.Current.User.Identity as System.Security.Principal.WindowsIdentity;
                var groupNames = new string[] { };
                if (windowsIdentity.Groups != null)
                    groupNames = windowsIdentity.Groups.Select(g => g.Translate(typeof(System.Security.Principal.NTAccount)).Value).ToArray();

                setCurrentAccessLevel(calculateAccessLevel(userName, groupNames));
            }
            // Now we are sure the value is stored
            return int.Parse(HttpContext.Current.Session["accesslevel"] as string);
        }

        public static void updateAccessLevel(String username)
        {
            HttpContext.Current.Session.Remove("accesslevel"); // invaidate current access level to force re-check

            var windowsIdentity = HttpContext.Current.User.Identity as System.Security.Principal.WindowsIdentity;
            var groupNames = new string[] { };
            if (windowsIdentity.Groups != null)
                groupNames = windowsIdentity.Groups.Select(g => g.Translate(typeof(System.Security.Principal.NTAccount)).Value).ToArray();


            setCurrentAccessLevel(calculateAccessLevel(username, groupNames));

            play.Logger.info("ACCESS LEVEL UPDATED TO " + int.Parse(HttpContext.Current.Session["accesslevel"] as string));
        }

        public static void setCurrentAccessLevel(String accessLevel)
        {
            HttpContext.Current.Session["accesslevel"] = accessLevel; // good for 2 hours
        }

        public static String calculateAccessLevel(String username, String[] groupnames)
        {
            // parse csv access levels file
            String accessFileURL = cpreader.Properties.Settings.Default.accessfile_url + cpreader.Properties.Settings.Default.accessfile_name;
            String accesslevel = "0";

            var DELIMITER = ',';

            bool onlineFile = cpreader.Properties.Settings.Default.accessfile_online;

            try
            {
                var linesText = onlineFile ? Encoding.UTF8.GetString(new WebClient().DownloadData(cpreader.Properties.Settings.Default.accessfile_url))
                    : System.IO.File.ReadAllText(cpreader.Properties.Settings.Default.accessfile_name);
                var lines = linesText
                    .Split(Environment.NewLine.ToArray(), StringSplitOptions.RemoveEmptyEntries)
                    .Where(line => !string.IsNullOrEmpty(line) && !line.Trim().StartsWith("#"));

                foreach (var line in lines)
                {
                    //Get all tokens available in line
                    String[] tokens = line.Split(DELIMITER);

                    // check group level access
                    if (groupnames != null && groupnames.Length > 0)
                    {
                        for (int i = 0; i < groupnames.Length; i++)
                        {
                            groupnames[i] = Regex.Replace(groupnames[i], "\\s", ""); // strip any white space
                            if (tokens[0].ToLower().Equals(groupnames[i].ToLower()))
                            {
                                if (tokens[1] != null)
                                {
                                    // we have a match, so return the access level
                                    tokens[1] = Regex.Replace(tokens[1], "\\s", ""); // strip any white space
                                    accesslevel = tokens[1];
                                }
                                else
                                {
                                    accesslevel = "0";
                                }

                                // set user cart usage
                                if (cpreader.Properties.Settings.Default.cart_enabled)
                                {
                                    // user specific cart access
                                    if (tokens[2] != null)
                                    {
                                        tokens[2] = Regex.Replace(tokens[2], "\\s", ""); // strip any white space
                                        HttpContext.Current.Session["usecart"] = "" + tokens[2];
                                    }
                                    else
                                    {
                                        HttpContext.Current.Session["usecart"] = "1";
                                    }
                                }
                            }
                        }
                    }

                    // check individual user level access (overrides group level access)
                    if (tokens[0].ToLower().Equals(username.ToLower()))
                    {
                        if (tokens[1] != null)
                        {
                            // we have a match, so return the access level
                            tokens[1] = Regex.Replace(tokens[1], "\\s", ""); // strip any white space
                            accesslevel = tokens[1];
                        }
                        else
                        {
                            accesslevel = "0";
                        }

                        // set user cart usage
                        if (cpreader.Properties.Settings.Default.cart_enabled)
                        {
                            if (tokens[2] != null)
                            {
                                tokens[2] = Regex.Replace(tokens[2], "\\s", ""); // strip any white space
                                                                                 // user specific cart access
                                if (int.Parse(tokens[2]) < 3)
                                {
                                    HttpContext.Current.Session["usecart"] = "" + tokens[2];
                                }
                                else
                                {
                                    HttpContext.Current.Session["usecart"] = "1";
                                }
                            }
                            else
                            {
                                HttpContext.Current.Session["usecart"] = "1";
                            }

                        }
                    }

                }
                // override any cart access if the master cart usage is set to false
                if (!cpreader.Properties.Settings.Default.cart_enabled)
                {
                    // disable for all
                    HttpContext.Current.Session["usecart"] = "0";
                }
            }
            catch (Exception e)
            {
                play.Logger.info("ERROR ");
            }

            return accesslevel; // lowest level access as default
        }
    }
}