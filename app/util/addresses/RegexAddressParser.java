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

package util.addresses;

import oio.sagdok.person._1_0.AdresseType;
import dk.oio.rep.xkom_dk.xml.schemas._2005._03._15.AddressAccessType;
import dk.oio.rep.xkom_dk.xml.schemas._2006._01._06.AddressCompleteType;
import dk.oio.rep.xkom_dk.xml.schemas._2006._01._06.AddressPostalType;
import oio.sagdok.person._1_0.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Beemen on 02/02/2015.
 */
public class RegexAddressParser implements IAddressParser {

    public AdresseType ToAddressType(String addressString) {
        String comma = "((\\s+)|(\\s*[,;\\.]{1}\\s*))";
        String pat = "(?<streetName>[^0-9]+)" + comma
                + "(?<houseNumber>[0-9]+[a-zA-Z]*)" + comma
                + "(" + "(?<floor>([0-9]{1,2})|st)?(\\.)?(sal)?" + comma + ")?"
                + "(" + "(?<door>[a-zA-Z]+)" + comma + ")?"
                + "(?<postCode>[0-9]{4})" + comma
                + "(?<postDistrict>\\p{L}+(\\s+\\p{L}+)*)\\Z";
                
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(addressString);
        if (m.matches()) {

            String streetName = null, houseNumber = null, floor = null, door = null, postCode = null, postDistrict = null;

            streetName = m.group("streetName");
            houseNumber = m.group("houseNumber");
            floor = m.group("floor");
            door = m.group("door");
            postCode = m.group("postCode");
            postDistrict = m.group("postDistrict");

            AdresseType ret = new AdresseType();
            DanskAdresseType danskAdresse = new DanskAdresseType();
            danskAdresse.setPostDistriktTekst(postDistrict);
            ret.setDanskAdresse(danskAdresse);

            AddressCompleteType addressComplete = new AddressCompleteType();
            danskAdresse.setAddressComplete(addressComplete);

            AddressAccessType addressAccess = new AddressAccessType();
            addressAccess.setStreetBuildingIdentifier(houseNumber);
            addressComplete.setAddressAccess(addressAccess);

            AddressPostalType addressPostal = new AddressPostalType();
            addressPostal.setStreetName(streetName);
            addressPostal.setFloorIdentifier(floor);
            addressPostal.setSuiteIdentifier(door);
            addressPostal.setPostCodeIdentifier(postCode);
            addressPostal.setDistrictName(postDistrict);
            addressComplete.setAddressPostal(addressPostal);

            return ret;
        }
        return null;
    }
}
