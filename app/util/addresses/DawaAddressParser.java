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

import dk.oio.rep.xkom_dk.xml.schemas._2005._03._15.AddressAccessType;
import dk.oio.rep.xkom_dk.xml.schemas._2006._01._06.AddressCompleteType;
import dk.oio.rep.xkom_dk.xml.schemas._2006._01._06.AddressPostalType;
import oio.sagdok.person._1_0.AdresseType;
import oio.sagdok.person._1_0.DanskAdresseType;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by Beemen on 02/02/2015.
 */
public class DawaAddressParser implements IAddressParser {

    public AdresseType ToAddressType(String addressString) {
        try {

            addressString = URLEncoder.encode(addressString,"UTF-8");
            String urlString = "http://dawa.aws.dk/adresser?q="+ addressString;
            URL url = new URL(urlString);

            Charset charset = Charset.forName("UTF-8");
            String response = IOUtils.toString(url, charset);

            JSONArray adresses = new JSONArray(response);

            if(adresses.length()>0){
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
                addressPostal.setDistrictSubdivisionIdentifier(districtSubdivision);
                addressPostal.setPostCodeIdentifier(postCode);
                addressPostal.setDistrictName(postDistrict);
                addressComplete.setAddressPostal(addressPostal);

                return ret;
            }
        }
        catch (Exception ex){
            play.Logger.error(ex.toString());
        }
        return null;
    }

    private String GetString(JSONObject obj, String path) throws JSONException{
        String[] names = path.split("/");
        for (int i=0 ; i<names.length-1 ; i++){
            obj = obj.getJSONObject(names[i]);
        }
        String ret = obj.getString(names[names.length -1 ]);
        if(ret.equals("null"))
            return null;
        else
            return ret;
    }

    private String GetString(JSONArray array, String path) throws JSONException{
        // Read value from first address
        JSONObject first = array.getJSONObject(0);
        String ret = GetString(first,path);

        // If null, then it was not specified in the query, return
        if(ret == null)
            return null;

        for (int i=1;i<array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            String ret2 = GetString(obj,path);

            // If different from first, then no value was specified, return
            if(!ret.equals(ret2))
                return null;
        }
        // Value was either specified or there is only one possible value, return it
        return ret;
    }
}
