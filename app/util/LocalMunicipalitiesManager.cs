using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.IO;
using System.Text;
using System.Net;

namespace cpreader.app.util
{
    public class LocalMunicipalitiesManager
    {
        public static int[] getLocalMunicipalities()
        {
            // parse csv access levels file
            var config = System.Web.Configuration.WebConfigurationManager.OpenWebConfiguration("~");
            var mainConfigPath = config.FilePath;
            String localMunicipalitiesFileName = Path.Combine(
                new FileInfo(mainConfigPath).Directory.FullName,
                string.Format("{0}", cpreader.Properties.Settings.Default.local_municipalities_name)
                );
            String accessFileURL = cpreader.Properties.Settings.Default.local_municipalities_file_url + localMunicipalitiesFileName;

            string linesText = "";
            try
            {
                linesText = System.IO.File.ReadAllText(localMunicipalitiesFileName);
            }
            catch (Exception e)
            {
                cpreader.Logger.info(String.Format("Error accessing \"local_municipalities\" configuration file: {0}", e));
            }

            int dummyInt;
            List<int> local_municipalities = new List<int>();
            try
            {
                var entries = linesText
                    .Split(Environment.NewLine.ToArray(), StringSplitOptions.RemoveEmptyEntries)
                    .Where(line => !string.IsNullOrEmpty(line) && !line.Trim().StartsWith("#"))
                    .Select(line => new
                    {
                        Code = (line.Length > 1 && int.TryParse(line, out dummyInt)) ? Convert.ToInt32(line) : null as int?,
                    });

                foreach (var entry in entries)
                {
                    if (entry.Code.HasValue)
                    {
                        local_municipalities.Add(entry.Code.Value);
                    }
                }
            }
            catch (Exception e)
            {
                cpreader.Logger.info(e.ToString());
            }

            return local_municipalities.ToArray();
        }
    }
}