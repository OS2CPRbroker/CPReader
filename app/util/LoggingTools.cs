using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net;
using System.Net.Sockets;
using System.Net.NetworkInformation;

namespace util
{
    class LoggingTools
    {
        public static IPAddress[] GetAllLocalIPv4(NetworkInterfaceType _type)
        {
            List<IPAddress> ipAddrList = new List<IPAddress>();
            foreach (NetworkInterface item in NetworkInterface.GetAllNetworkInterfaces())
            {
                if (item.NetworkInterfaceType == _type && item.OperationalStatus == OperationalStatus.Up)
                {
                    foreach (UnicastIPAddressInformation ip in item.GetIPProperties().UnicastAddresses)
                    {
                        if (ip.Address.AddressFamily == AddressFamily.InterNetwork)
                        {
                            ipAddrList.Add(ip.Address);
                        }
                    }
                }
            }
            return ipAddrList.ToArray();
        }

        /// <summary>
        /// method to get Client ip address
        /// </summary>
        /// <param name="GetLan"> set to true if want to get local(LAN) Connected ip address</param>
        /// <returns></returns>
        public static String GetVisitorIPAddress(bool GetLan = false)
        {
            String visitorIPAddress = HttpContext.Current.Request.ServerVariables["HTTP_X_FORWARDED_FOR"];

            if (String.IsNullOrEmpty(visitorIPAddress))
                visitorIPAddress = HttpContext.Current.Request.ServerVariables["REMOTE_ADDR"];

            if (String.IsNullOrEmpty(visitorIPAddress))
                visitorIPAddress = HttpContext.Current.Request.UserHostAddress;

            if (String.IsNullOrEmpty(visitorIPAddress) || visitorIPAddress.Trim() == "::1")
            {
                GetLan = true;
                visitorIPAddress = String.Empty;
            }

            if (GetLan && String.IsNullOrEmpty(visitorIPAddress))
            {
                IPAddress[] IPAddressEthernet = GetAllLocalIPv4(NetworkInterfaceType.Ethernet);
                IPAddress[] IPAddressWireless = GetAllLocalIPv4(NetworkInterfaceType.Wireless80211);

                IPAddress[] arrIpAddress = IPAddressEthernet.Any() ? IPAddressEthernet : IPAddressWireless.Any() ? IPAddressWireless: new IPAddress[0];

                visitorIPAddress = arrIpAddress.FirstOrDefault().ToString();
                if (!String.IsNullOrEmpty(visitorIPAddress))
                {
                    return visitorIPAddress;
                }
                try
                {
                    String stringHostName = Dns.GetHostName();
                    arrIpAddress = Dns.GetHostAddresses(stringHostName);
                    visitorIPAddress = arrIpAddress.FirstOrDefault().ToString();
                }
                catch (Exception ex)
                {
                    visitorIPAddress = "Unable to determine client IP Address";
                }

            }
            return visitorIPAddress;
        }

        public static String GetHostName(String ipAddress)
        {
            String hostName = "";
            try
            {
                hostName = System.Net.Dns.GetHostEntry(ipAddress).HostName.Split(new Char[] { '.' }).FirstOrDefault<String>();
            }
            catch (Exception ex)
            {
                hostName = "Unable to determine host name";
            }
            return hostName;
        }
    }
}