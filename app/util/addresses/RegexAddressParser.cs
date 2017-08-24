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

using cpreader.PartService;
using System;
using System.Text.RegularExpressions;

namespace util.addresses
{

    /**
     * Created by Beemen on 02/02/2015.
     */
    public class RegexAddressParser : IAddressParser
    {

        public AdresseType ToAddressType(String addressString)
        {
            String comma = "((\\s+)|(\\s*[,;\\.]{1,2}\\s*))";
            String pat = "(?<streetName>[^0-9]+)" + comma
                    + "(?<houseNumber>[0-9]+[a-zA-Z]*)" + comma
                    + "(" + "(?<floor>([0-9]{1,2})|st)?(\\.)?(sal)?" + comma + ")?"
                    + "(" + "(?<door>[a-zA-Z]+)" + comma + ")?"
                    + "(?<postCode>[0-9]{4})" + comma
                    + "(?<postDistrict>\\p{L}+(\\s+\\p{L}+)*)\\Z";

            Match m = Regex.Match(addressString, pat);
            if (m.Success)
            {

                String streetName = null, houseNumber = null, floor = null, door = null, postCode = null, postDistrict = null;

                streetName = m.Groups["streetName"].Value;
                houseNumber = m.Groups["houseNumber"].Value;
                floor = m.Groups["floor"].Value;
                door = m.Groups["door"].Value;
                postCode = m.Groups["postCode"].Value;
                postDistrict = m.Groups["postDistrict"].Value;

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
                addressPostal.PostCodeIdentifier = postCode;
                addressPostal.DistrictName = postDistrict;
                addressComplete.AddressPostal = addressPostal;

                return ret;
            }
            return null;
        }
    }
}