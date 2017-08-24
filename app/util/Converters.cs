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
using System.Collections.Generic;
using cpreader.PartService;
using System.Text.RegularExpressions;
using util.cprbroker;
using util.addresses;

namespace util
{
    /**
     * Created by Beemen on 11/11/2014.
     */
    public class Converters
    {
        public static readonly IFormatProvider danishDateFormat = System.Globalization.CultureInfo.GetCultureInfo("da-DK").DateTimeFormat;

        public static IAddressParser AddressParser = new DawaAddressParser();
        public static bool isDawaAddressParser()
        {
            return AddressParser is DawaAddressParser;
        }

        public AdresseType ToAddressType(String addressString)
        {
            addressString = string.Format("{0}", addressString);
            if (!string.IsNullOrEmpty(addressString))
            {
                return AddressParser.ToAddressType(addressString);
            }
            return null;
        }

        public NavnStrukturType ToNavnStrukturType(string name, int? type = null)
        {

            if (string.IsNullOrEmpty(name))
                return null;

            while (name.Contains(","))
            {
                int i = name.LastIndexOf(",");
                name = name.Substring(i + 1).Trim(' ') + " " + name.Substring(0, i).Trim(' ');
            }

            String firstName = null, middleName = null, lastName = null;
            String[] arr = name.Split(' ');

            if (type != null)
            {
                switch (type)
                {
                    case 0:
                        firstName = arr[0];
                        break;
                    case 1:
                        middleName = arr[0];
                        break;
                    case 2:
                        lastName = arr[0];
                        break;
                    default:
                        break;
                }
            }
            else
            {
                if (arr.Length >= 1)
                    firstName = arr[0];
                if (arr.Length > 1)
                    lastName = arr[arr.Length - 1];
                if (arr.Length > 2)
                {
                    middleName = "";
                    var middleNames = new List<string>();
                    for (int i = 1; i < arr.Length - 1; i++)
                    {
                        middleNames.Add(arr[i]);
                    }
                    middleName = StringUtils.join(" ", middleNames).Trim(' ');
                }
            }
            
            //CPRBroker will search for people matching ALL criterias, that are not null, so this nullifies the names, if they are empty,
            //This could happen if 2 spaces are between names
            //This is also usefull, to make searching for someone with only a lastname possible by searching for LASTNAME,
            if(firstName == "")
            {
                firstName = null;
            }
            if(middleName == "")
            {
                middleName = null;
            }
            if(lastName == "")
            {
                lastName = null;
            }

            return ToNavnStrukturType(firstName, middleName, lastName);
        }

        public NavnStrukturType ToNavnStrukturType(String firstname, String middlename, String lastname)
        {
            // Set the name search criteria
            PersonNameStructureType nameStructure = new PersonNameStructureType();
            nameStructure.PersonGivenName = firstname;
            nameStructure.PersonMiddleName = middlename;
            nameStructure.PersonSurnameName = lastname;

            // Playing the matryoshka doll game
            NavnStrukturType navnStrukturType = new NavnStrukturType();
            navnStrukturType.PersonNameStructure = nameStructure;
            return navnStrukturType;
        }

        public String[] ToPostalLabel(IPerson person)
        {
            // TODO: There might be some Regex errors here that do not work exactly in C# as in Java
            String newLine = Environment.NewLine;

            // name
            String nameString = StringUtils.format("{0} {1} {2}",
                    person.firstname(),
                    person.middelname(),
                    person.lastname()
            );
            nameString = Regex.Replace(nameString, "\\s{2,}", " ");


            // Address
            String addressString = "";
            if (person.address() != null)
            {
                IAddress address = person.address();


                switch (person.address().addressType())
                {
                    case EAddressType.Danish:
                        IDanishAddress danishAddress = address.danishAddress();
                        addressString = StringUtils.format("{0} {1} {2} {3}", danishAddress.streetName(), danishAddress.streetBuildingIdentifier(), danishAddress.floor(), danishAddress.suite()) + newLine
                                + StringUtils.format("{0}", danishAddress.districtSubdivision()) + newLine
                                + StringUtils.format("{0} {1}", danishAddress.postCode(), danishAddress.postDistrikt());
                        break;
                    case EAddressType.Greenlandic:
                        IGreenlandicAddress greenlandicAddress = address.greenlandicAddress();
                        addressString = StringUtils.format("{0} {1} {2} {3}", greenlandicAddress.streetName(), greenlandicAddress.streetBuilding(), greenlandicAddress.floor(), greenlandicAddress.suite()) + newLine
                                + StringUtils.format("{0}", greenlandicAddress.districtSubdivision()) + newLine
                                + StringUtils.format("{0} {1}", greenlandicAddress.postCode(), greenlandicAddress.districtName());
                        break;
                    case EAddressType.World:
                        IWorldAddress worldAddress = address.worldAddress();
                        addressString = string.Format("{0}", worldAddress.postalAddressFirstLineText()) + newLine
                                + StringUtils.format("{0}", worldAddress.postalAddressSecondLineText()) + newLine
                                + StringUtils.format("{0}", worldAddress.postalAddressThirdLineText()) + newLine
                                + StringUtils.format("{0}", worldAddress.postalAddressFourthLineText()) + newLine
                                + StringUtils.format("{0}", worldAddress.postalAddressFifthLineText()) + newLine;
                        break;
                }
                addressString = Regex.Replace(addressString, " " + newLine, newLine);
                addressString = Regex.Replace(addressString, "[ ]{2,}", " ");
                addressString = Regex.Replace(addressString, "(" + newLine + "){2,}", newLine);
            }

            String ret = nameString + newLine + addressString;
            ret = Regex.Replace(ret, "(" + newLine + "){2,}", newLine);
            return ret.Split(newLine.ToCharArray(), StringSplitOptions.RemoveEmptyEntries);
        }

        public SoegObjektType AddressToSoegObjekt(IAddress address)
        {
            if (address == null)
                return null;

            SoegAttributListeType soegAttributListeType = new SoegAttributListeType();

            // Now fill Egenskab & registerOplysning
            SoegEgenskabType soegEgenskabType = new SoegEgenskabType();
            soegEgenskabType.NavnStruktur = null;
            soegAttributListeType.SoegEgenskab = new SoegEgenskabType[] { soegEgenskabType };

            if (address != null)
            {
                RegisterOplysningType registerOplysningType = new RegisterOplysningType();

                CprBorgerType cprBorgerType = new CprBorgerType();
                AdresseType addressObject = getAdresseType(address);
                if (addressObject == null)
                    return null;

                cprBorgerType.FolkeregisterAdresse = addressObject;
                registerOplysningType.Item = cprBorgerType;

                soegAttributListeType.SoegRegisterOplysning = new RegisterOplysningType[] { registerOplysningType };
            }

            SoegObjektType soegObjekt = new SoegObjektType();
            soegObjekt.SoegAttributListe = soegAttributListeType;
            return soegObjekt;
        }

        private AdresseType getAdresseType(IAddress address)
        {
            AdresseType ret = new AdresseType();
            switch (address.addressType())
            {
                case EAddressType.Danish:
                    ret = getAdresseType(address as IDanishAddress);
                    break;

                default:
                    ret = null;                   
                    break;
            }

            return ret;
        }

        private AdresseType getAdresseType(IDanishAddress address)
        {
            String streetName = null, houseNumber = null, floor = null, door = null, districtSubdivision = null, postCode = null, postDistrict = null;

            streetName = address.streetName();
            houseNumber = address.streetBuilding();
            floor = address.floor();
            door = address.suite();
            districtSubdivision = address.districtSubdivision();
            postCode = address.postCode();
            postDistrict = address.postDistrikt();

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

        public List<SoegObjektType> ToSoegObjektTypes(String name, String address)
        {
            List<NavnStrukturType> navnStrukturTypes = new List<NavnStrukturType>();
            List<SoegObjektType> soegObjekts = new List<SoegObjektType>();
            if (name != "" && name.Split(' ').Length == 1)
            {
                for (int i = 0; i < 3; i++)
                {
                    navnStrukturTypes.Add(ToNavnStrukturType(name, i));
                }
            }
            else
            {
                navnStrukturTypes.Add(ToNavnStrukturType(name));
            }

            foreach(NavnStrukturType navnStrukturType in navnStrukturTypes)
            {
                AdresseType addressObject = ToAddressType(address);
                if (navnStrukturType == null && addressObject == null)
                    return null;

                SoegAttributListeType soegAttributListeType = new SoegAttributListeType();

                // Now fill Egenskab & registerOplysning
                SoegEgenskabType soegEgenskabType = new SoegEgenskabType();
                soegEgenskabType.NavnStruktur = navnStrukturType;
                soegAttributListeType.SoegEgenskab = new SoegEgenskabType[] { soegEgenskabType };

                if (addressObject != null)
                {
                    RegisterOplysningType registerOplysningType = new RegisterOplysningType();

                    CprBorgerType cprBorgerType = new CprBorgerType();
                    cprBorgerType.FolkeregisterAdresse = addressObject;
                    registerOplysningType.Item = cprBorgerType;

                    soegAttributListeType.SoegRegisterOplysning = new RegisterOplysningType[] { registerOplysningType };
                }

                SoegObjektType soegObjekt = new SoegObjektType();
                soegObjekt.SoegAttributListe = soegAttributListeType;

                soegObjekts.Add(soegObjekt);
            }
            
            return soegObjekts;
        }
    }
}