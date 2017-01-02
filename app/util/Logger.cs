using System;
using log4net;
using System.IO;
using log4net.Core;
using System.Web;

namespace play
{

    /**
     * Created by Mat Howlett on 03/03/2015.
     */
    public class Logger
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(object));

        static Logger()
        {
            var config = System.Web.Configuration.WebConfigurationManager.OpenWebConfiguration("~");
            var mainConfigPath = config.FilePath;
            var path = Path.Combine(
                new FileInfo(mainConfigPath).Directory.FullName,
                string.Format("{0}", cpreader.Properties.Settings.Default.cpreader_Log4NetConfig)
                );

            log4net.Config.XmlConfigurator.ConfigureAndWatch(new FileInfo(path));
        }

        public static void Log(Level level, string text)
        {
            var logEvent = new LoggingEvent(new LoggingEventData()
            {
                Level = level,
                Message = text,
                TimeStamp = DateTime.Now,
                UserName = HttpContext.Current.User.Identity.Name,
                ExceptionString = null,
                Domain = null,
                Identity = null,
                LocationInfo = null,
                LoggerName = null,
                //Properties = props,
                ThreadName = null
            });

            log.Logger.Log(logEvent);
        }

        public static void error(Exception ex)
        {
            Log(Level.Error, ex.ToString());
        }

        public static void error(string s)
        {
            Log(Level.Error, s);
        }

        public static void debug(string msg)
        {
            Log(Level.Debug, msg);
        }

        public static void info(string msg)
        {
            Log(Level.Info, msg);
        }        
    }
}