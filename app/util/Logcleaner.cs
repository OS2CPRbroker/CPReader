using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Caching;
using System.IO;
using System.Text.RegularExpressions;

namespace cpreader.app.util
{
    public class Logcleaner
    {
        private static CacheItemRemovedCallback OnCacheRemove = null;

        public static void AddTask(string name, int seconds)
        {
            OnCacheRemove = new CacheItemRemovedCallback(CacheItemRemoved);
            HttpRuntime.Cache.Insert(name, seconds, null,
                DateTime.Now.AddSeconds(seconds), Cache.NoSlidingExpiration,
                CacheItemPriority.NotRemovable, OnCacheRemove);
        }

        private static void CacheItemRemoved(string k, object v, CacheItemRemovedReason r)
        {
            switch (k)
            {
                case "CleanOldLogs":
                    CleanOldLogs();
                    break;
                default:
                    Logger.error("Unrecognized task");
                    break;
            } 
            AddTask(k, Convert.ToInt32(v));
        }

        private static void CleanOldLogs()
        {
            //Logger.info("Deleting old log files...");
            Console.WriteLine("Log cleaner running");

            DateTime dateToday = DateTime.Today;
            string root = HttpRuntime.AppDomainAppPath;
            string logsfolder = "logs";
            string path = root + logsfolder;
            string[] fileEntries = Directory.GetFiles(path);

            // Check the date of all files in the logs folder. If the date is older than 6 months, delete the file.
            foreach (string filename in fileEntries)
            {
                Match date = Regex.Match(filename, cpreader.app.util.Constants.DateRegex);
                if (!String.IsNullOrEmpty(date.Value))
                {
                    DateTime dateCreated = DateTime.MaxValue;
                    if (DateTime.TryParse(date.Value, out dateCreated))
                    {
                        TimeSpan fileAge = dateToday.Subtract(dateCreated);
                        string strDate = dateCreated.ToString();
                        string strAge = fileAge.ToString();
                        
                        if (fileAge.Days > cpreader.app.util.Constants.MaxLogfileAge)
                        {
                            DeleteFile(filename);
                        }
                    }
                }
            }
        }

        private static void DeleteFile(string filename)
        {
            if (File.Exists(filename))
            {
                File.Delete(filename);
                //Logger.info(String.Format("Deleting log file {0}", filename));
            }
            else
            {
                //Logger.error(String.Format("Error deleting log file {0}", filename));
            }
        }
    }
}