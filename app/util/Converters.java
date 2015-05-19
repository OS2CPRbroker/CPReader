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

 package util;

import dk.oio.rep.itst_dk.xml.schemas._2006._01._17.PersonNameStructureType;
import oio.sagdok.person._1_0.*;
import util.addresses.IAddressParser;
import util.cprbroker.*;
import java.util.ArrayList;

/**
 * Created by Beemen on 11/11/2014.
 */
public class Converters {


    public static IAddressParser AddressParser;

    public AdresseType ToAddressType(String addressString) {
        if (addressString != null && !addressString.trim().isEmpty()) {
            return this.AddressParser.ToAddressType(addressString);
        }
        return null;
    }

    public NavnStrukturType ToNavnStrukturType(String name) {

        if (name == null || name.isEmpty())
            return null;

        while (name.contains(",")) {
            int i = name.lastIndexOf(",");
            name = name.substring(i + 1) + " " + name.substring(0, i - 1);
        }

        String firstName = null, middleName = null, lastName = null;
        String[] arr = name.split(" ");
        if (arr.length >= 1)
            firstName = arr[0];
        if (arr.length > 1)
            lastName = arr[arr.length - 1];
        if (arr.length > 2) {
            middleName = "";
            ArrayList<String> middleNames = new ArrayList<String>();
            for (int i = 1; i < arr.length - 1; i++) {
                middleNames.add(arr[i]);
            }
            middleName = StringUtils.join(" ", middleNames);
        }
        return ToNavnStrukturType(firstName, middleName, lastName);
    }

    public NavnStrukturType ToNavnStrukturType(String firstname, String middlename, String lastname) {
        // Set the name search criteria
        PersonNameStructureType nameStructure = new PersonNameStructureType();
        nameStructure.setPersonGivenName(firstname);
        nameStructure.setPersonMiddleName(middlename);
        nameStructure.setPersonSurnameName(lastname);

        // Playing the matryoshka doll game
        NavnStrukturType navnStrukturType = new NavnStrukturType();
        navnStrukturType.setPersonNameStructure(nameStructure);
        return navnStrukturType;
    }


    public String[] ToPostalLabel(IPerson person) {

        String newLine = System.getProperty("line.separator");

        // name
        String nameString = StringUtils.format("%s %s %s",
                person.firstname(),
                person.middelname(),
                person.lastname()
        );
        nameString = nameString.replaceAll("\\s{2,}", " ");


        // Address
        String addressString = "";
        if (person.address() != null) {
            IAddress address = person.address();


            switch (person.address().addressType()) {
                case Danish:
                    IDanishAddress danishAddress = address.danishAddress();
                    addressString = StringUtils.format("%s %s %s %s", danishAddress.streetName(), danishAddress.streetBuildingIdentifier(), danishAddress.floor(), danishAddress.suite()) + newLine
                            + StringUtils.format("%s", danishAddress.districtSubdivision()) + newLine
                            + StringUtils.format("%s %s", danishAddress.postCode(), danishAddress.postDistrikt());
                    break;
                case Greenlandic:
                    IGreenlandicAddress greenlandicAddress = address.greenlandicAddress();
                    addressString = StringUtils.format("%s %s %s %s", greenlandicAddress.streetName(), greenlandicAddress.streetBuilding(), greenlandicAddress.floor(), greenlandicAddress.suite()) + newLine
                            + StringUtils.format("%s", greenlandicAddress.districtSubdivision()) + newLine
                            + StringUtils.format("%s %s", greenlandicAddress.postCode(), greenlandicAddress.districtName());
                    break;
                case World:
                    IWorldAddress worldAddress = address.worldAddress();
                    addressString = String.format("%s", worldAddress.postalAddressFirstLineText()) + newLine
                            + StringUtils.format("%s", worldAddress.postalAddressSecondLineText()) + newLine
                            + StringUtils.format("%s", worldAddress.postalAddressThirdLineText()) + newLine
                            + StringUtils.format("%s", worldAddress.postalAddressFourthLineText()) + newLine
                            + StringUtils.format("%s", worldAddress.postalAddressFifthLineText()) + newLine;
                    break;
            }
            addressString = addressString.replaceAll(" " + newLine, newLine);
            addressString = addressString.replaceAll("[ ]{2,}", " ");
            addressString = addressString.replaceAll("(" + newLine + "){2,}", newLine);
        }

        String ret = nameString + newLine + addressString;
        ret = ret.replaceAll("(" + newLine + "){2,}", newLine);
        return ret.split(newLine);
    }

    public SoegObjektType ToSoegObjektType(String name, String address) {

        NavnStrukturType navnStrukturType = ToNavnStrukturType(name);
        AdresseType addressObject = ToAddressType(address);
        if(navnStrukturType == null && addressObject == null)
            return null;

        SoegAttributListeType soegAttributListeType = new SoegAttributListeType();

        // Now fill Egenskab & registerOplysning
        SoegEgenskabType soegEgenskabType = new SoegEgenskabType();
        soegEgenskabType.setNavnStruktur(navnStrukturType);
        soegAttributListeType.getSoegEgenskab().add(soegEgenskabType);

        if (addressObject != null) {
            RegisterOplysningType registerOplysningType = new RegisterOplysningType();

            CprBorgerType cprBorgerType = new CprBorgerType();
            cprBorgerType.setFolkeregisterAdresse(addressObject);
            registerOplysningType.setCprBorger(cprBorgerType);

            soegAttributListeType.getSoegRegisterOplysning().add(registerOplysningType);
        }

        SoegObjektType soegObjekt = new SoegObjektType();
        soegObjekt.setSoegAttributListe(soegAttributListeType);

        return soegObjekt;
    }


}
