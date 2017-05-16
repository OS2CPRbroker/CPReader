/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 2.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 * Beemen Beshara
 *
 * The code is currently governed by OS2 - Offentligt digitaliserings-
 * fællesskab / http://www.os2web.dk .
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

using System;
using cpreader.PartService;
using System.Web;
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace util.addresses
{

    /**
     * Created by Beemen on 02/02/2015.
     */
    public class DawaAddressParser : IAddressParser
    {
        public AdresseType ToAddressType(String addressString)
        {
            try
            {

                addressString = HttpUtility.UrlEncode(addressString, Encoding.UTF8);
                String urlString = "http://dawa.aws.dk/adresser?q=" + addressString;
                var url = new Uri(urlString);
                var responseBytes = new System.Net.WebClient().DownloadData(url);
                var charset = Encoding.UTF8;
                String response = charset.GetString(responseBytes);

                JArray adresses = JArray.Parse(response);

                if (adresses.Count > 0)
                {
                    String streetName = null, houseNumber = null, floor = null, door = null, districtSubdivision = null, postCode = null, postDistrict = null;

                    streetName = GetString(adresses, "adgangsadresse/vejstykke/navn");
                    houseNumber = GetString(adresses, "adgangsadresse/husnr");
                    floor = GetString(adresses, "etage");
                    door = GetString(adresses, "dør");
                    districtSubdivision = GetString(adresses, "adgangsadresse/supplerendebynavn");
                    postCode = GetString(adresses, "adgangsadresse/postnummer/nr");
                    postDistrict = GetString(adresses, "adgangsadresse/postnummer/navn");

                    AdresseType ret = new AdresseType();
                    DanskAdresseType danskAdresse = new DanskAdresseType();
                    danskAdresse.PostDistriktTekst = postDistrict;
                    ret.Item = danskAdresse;

                    AddressCompleteType addressComplete = new AddressCompleteType();
                    danskAdresse.AddressComplete = addressComplete;

                    AddressAccessType addressAccess = new AddressAccessType();
                    addressAccess.StreetBuildingIdentifier = houseNumber;
                    addressComplete.AddressAccess = addressAccess;

                    AddressPostalType addressPostal = new AddressPostalType();
                    addressPostal.StreetName = streetName;
                    addressPostal.FloorIdentifier = floor;
                    addressPostal.SuiteIdentifier = door;
                    addressPostal.DistrictSubdivisionIdentifier = districtSubdivision;
                    addressPostal.PostCodeIdentifier = postCode;
                    addressPostal.DistrictName = postDistrict;
                    addressComplete.AddressPostal = addressPostal;

                    return ret;
                }
            }
            catch (Exception ex)
            {
                play.Logger.error(ex);
            }
            return null;
        }

        private String GetString(JToken obj, String path)
        {
            String[]
            names = path.Split('/');
            for (int i = 0; +i < names.Length - 1; i++)
            {
                obj = obj[names[i]];
            }
            // TODO: Make sure this line gets the correct JSON value
            var ret = obj[names[names.Length - 1]].ToString();
            if (ret.Equals("null"))
                return null;
            else
                return ret;
        }

        private String GetString(JArray array, String path)
        {
            // Read value from first address
            var first = array.First;
            String ret = GetString(first, path);

            // If null, then it was not specified in the query, return
            if (ret == null)
                return null;

            // TODO: make sure this piece workd after C# conversion
            for (int i = 1; i < array.Count; i++)
            {
                var obj = array[i];
                String ret2 = GetString(obj, path);

                // If different from first, then no value was specified, return
                if (!ret.Equals(ret2))
                    return null;
            }
            // Value was either specified or there is only one possible value, return it
            return ret;
        }
    }
}