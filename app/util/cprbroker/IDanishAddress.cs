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
 * Søren Kirkegård
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

namespace util.cprbroker
{

    public interface IDanishAddress : IAddress
    {


        /*
        danishAddress.getAddressComplete().getAddressAccess().getMunicipalityCode()
        danishAddress.getAddressComplete().getAddressAccess().getStreetBuildingIdentifier()
        danishAddress.getAddressComplete().getAddressAccess().getStreetCode()

        danishAddress.getAddressComplete().getAddressPostal().getCountryIdentificationCode()
        danishAddress.getAddressComplete().getAddressPostal().getDistrictName()
        danishAddress.getAddressComplete().getAddressPostal().getDistrictSubdivisionIdentifier()
        danishAddress.getAddressComplete().getAddressPostal().getFloorIdentifier()
        danishAddress.getAddressComplete().getAddressPostal().getMailDeliverySublocationIdentifier()
        danishAddress.getAddressComplete().getAddressPostal().getPostCodeIdentifier()
        danishAddress.getAddressComplete().getAddressPostal().getPostOfficeBoxIdentifier()
        danishAddress.getAddressComplete().getAddressPostal().getStreetBuildingIdentifier()	
        danishAddress.getAddressComplete().getAddressPostal().getStreetName()
        danishAddress.getAddressComplete().getAddressPostal().getStreetNameForAddressingName()
        danishAddress.getAddressComplete().getAddressPostal().getSuiteIdentifier()

        danishAddress.getNoteTekst()
        danishAddress.getPolitiDistriktTekst()
        danishAddress.getPostDistriktTekst()
        danishAddress.getSkoleDistriktTekst()
        danishAddress.getSocialDistriktTekst()
        danishAddress.getSogneDistriktTekst()
        danishAddress.getValgkredsDistriktTekst()
        danishAddress.isSpecielVejkodeIndikator()
        danishAddress.isUkendtAdresseIndikator()

        //TODO Implement these another day - not yet implemented in the CPR Broker!
        danishAddress.getAddressPoint().getAddressPointIdentifier()
        danishAddress.getAddressPoint().getAddressPointStatusStructure().getAddressCoordinateQualityClassCode().name()
        danishAddress.getAddressPoint().getAddressPointStatusStructure().getAddressPointRevisionDateTime()
        danishAddress.getAddressPoint().getAddressPointStatusStructure().getAddressPointValidEndDateTime()
        danishAddress.getAddressPoint().getAddressPointStatusStructure().getAddressPointValidStartDateTime()
        danishAddress.getAddressPoint().getGeographicPointLocation().getCrsIdentifier()
        danishAddress.getAddressPoint().getGeographicPointLocation().getGeographicCoordinateTuple().getGeographicEastingMeasure()
        danishAddress.getAddressPoint().getGeographicPointLocation().getGeographicCoordinateTuple().getGeographicHeightMeasure()
        danishAddress.getAddressPoint().getGeographicPointLocation().getGeographicCoordinateTuple().getGeographicNorthingMeasure()

         */

        String municipalityCode();
        String streetBuildingIdentifier();
        String streetCode();


        String countryIdentificationCode();
        String districtName();
        String districtSubdivision();
        String floor();
        String mailSublocation();
        String postCode();
        String postOfficeBox();
        String streetBuilding();
        String streetName();
        String streetNameForAddressing();
        String suite();

        String danishNote();
        String politiDistrikt();
        String postDistrikt();
        String skoleDistrikt();
        String socialDistrikt();
        String sogneDistrikt();
        String valgkredsDistrikt();
        Boolean isSpecielVejkode();
        Boolean isUkendtAdresse();


    }
}