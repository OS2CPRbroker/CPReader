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
using System.Collections.Generic;
namespace util.cprbroker.models
{




public class DanishAddress : IDanishAddress {

	public String _municipalityCode;
	public String _streetBuildingIdentifier;
	public String _streetCode;
	
	public String _countryIdentificationCode;
	public String _districtName;
	public String _districtSubdivision;
	public String _floor;
	public String _mailSubLocaltion;
	public String _postCode;
	public String _postOfficeBox;
	public String _streetBuilding;
	public String _streetName;
	public String _streetNameForAdressing;
	public String _suite;
	public String _note;

	public String _danishNote;
	public String _politiDistrikt;
	public String _postDistrikt;
	public String _skoleDistrikt;
	public String _socialDistrikt;
	public String _sogneDistrikt;
	public String _valgkredsDistrikt;
	public Boolean _isSpecielVejkode;
	public Boolean _isUkendtAdresse;

	
	public class Builder : IBuilder<IDanishAddress> {

		public String _countryIdentificationCode;
		public String _districtName;
		public String _districtSubdivision;
		public String _floor;
		public String _mailSubLocaltion;
		public String _postCode;
		public String _postOfficeBox;
		public String _streetBuilding;
		public String _streetName;
		public String _streetNameForAdressing;
		public String _suite;
		public String _note;

		public String _municipalityCode;
		public String _streetBuildingIdentifier;
		public String _streetCode;

		public String _danishNote;
		public String _politiDistrikt;
		public String _postDistrikt;
		public String _skoleDistrikt;
		public String _socialDistrikt;
		public String _sogneDistrikt;
		public String _valgkredsDistrikt;
		public Boolean _isSpecielVejkode;
		public Boolean _isUkendtAdresse;
		
		// build method
		public IDanishAddress build() { return new DanishAddress(this); }
		
		// builder methods
		public Builder countryIdentificationCode(String newCountryIdentificationCode) { _countryIdentificationCode = newCountryIdentificationCode.TrimZerosOnLeft(); return this;}
		public Builder districtName(String newDistrictName) { _districtName = newDistrictName; return this;}
		public Builder districtSubdivision(String newDistrictSubdivision) { _districtSubdivision = newDistrictSubdivision; return this;}
		public Builder floor(String newFloor) { _floor = newFloor.TrimZerosOnLeft(); return this;}
		public Builder mailSubLocaltion(String newMailSubLocaltion) { _mailSubLocaltion = newMailSubLocaltion; return this;}
		public Builder postCode(String newPostCode) { _postCode = newPostCode.TrimZerosOnLeft(); return this;}
		public Builder postOfficeBox(String newPostOfficeBox) { _postOfficeBox = newPostOfficeBox; return this;}
		public Builder streetBuilding(String newStreetBuilding) { _streetBuilding = newStreetBuilding.TrimZerosOnLeft(); return this;}
		public Builder streetName(String newStreetName) { _streetName = newStreetName; return this;}
		public Builder streetNameForAdressing(String newStreetNameForAdressing) { _streetNameForAdressing = newStreetNameForAdressing; return this;}
		public Builder suite(String newSuite) { _suite = newSuite.TrimZerosOnLeft(); return this;}
		public Builder note(String newNote) { _note = newNote; return this;}
		
		public Builder municipalityCode(String newCode) { _municipalityCode = newCode.TrimZerosOnLeft(); return this;}
		public Builder streetBuildingIdentifier(String newIdentifier) { _streetBuildingIdentifier = newIdentifier.TrimZerosOnLeft(); return this;}
		public Builder streetCode(String newCode) { _streetCode = newCode.TrimZerosOnLeft(); return this;}
		
		public Builder danishNote(String newNote) { _danishNote = newNote; return this;}
		public Builder politiDistrikt(String newDistrikt) { _politiDistrikt = newDistrikt; return this;}
		public Builder postDistrikt(String newDistrikt) { _postDistrikt = newDistrikt; return this;}
		public Builder skoleDistrikt(String newDistrikt) { _skoleDistrikt = newDistrikt; return this;}
		public Builder socialDistrikt(String newDistrikt) { _socialDistrikt = newDistrikt; return this;}
		public Builder sogneDistrikt(String newDistrikt) { _sogneDistrikt = newDistrikt; return this;}
		public Builder valgkredsDistrikt(String newDistrikt) { _valgkredsDistrikt = newDistrikt; return this;}
		public Builder isSpecielVejkode(Boolean isSpecial) { _isSpecielVejkode = isSpecial; return this;}
		public Builder isUkendtAdresse(Boolean isUkendt) { _isUkendtAdresse = isUkendt; return this;}
		
	}
	
	private DanishAddress(Builder builder) {
		_countryIdentificationCode = builder._countryIdentificationCode;
		_districtName = builder._districtName;
		_districtSubdivision = builder._districtSubdivision;
		_floor = StringUtils.TrimZerosOnLeft(builder._floor);
		_mailSubLocaltion = builder._mailSubLocaltion;
		_postCode = builder._postCode;
		_postOfficeBox = builder._postOfficeBox;
		_streetBuilding = StringUtils.TrimZerosOnLeft(builder._streetBuilding);
		_streetName = builder._streetName;
		_streetNameForAdressing = builder._streetNameForAdressing;
		_suite = StringUtils.TrimZerosOnLeft(builder._suite);
		_note = builder._note;

		_municipalityCode = StringUtils.TrimZerosOnLeft(builder._municipalityCode);
		_streetBuildingIdentifier = StringUtils.TrimZerosOnLeft(builder._streetBuildingIdentifier);
		_streetCode = StringUtils.TrimZerosOnLeft(builder._streetCode);

		_danishNote = builder._danishNote;
		_politiDistrikt = builder._politiDistrikt;
		_postDistrikt = builder._postDistrikt;
		_skoleDistrikt = builder._skoleDistrikt;
		_socialDistrikt = builder._socialDistrikt;
		_sogneDistrikt = builder._sogneDistrikt;
		_valgkredsDistrikt = builder._valgkredsDistrikt;
		_isSpecielVejkode = builder._isSpecielVejkode;
		_isUkendtAdresse = builder._isUkendtAdresse;

	}
	
	// no-brainers - this is a danish address
	
	public EAddressType addressType() {	return EAddressType.Danish; }
	 public IDanishAddress danishAddress() { return this; }
	 public IGreenlandicAddress greenlandicAddress() { throw new InvalidOperationException(); }
	 public IWorldAddress worldAddress() { throw new InvalidOperationException(); }

	
	public String countryIdentificationCode() {	return _countryIdentificationCode; }

	
	public String districtName() { return _districtName;	}

	
	public String districtSubdivision() { return _districtSubdivision;}

	
	public String floor() {	return _floor;}

	
	public String mailSublocation() { return _mailSubLocaltion;	}

	
	public String postCode() { return _postCode;	}

	
	public String postOfficeBox() {	return _postOfficeBox; }

	
	public String streetBuilding() { return _streetBuilding; }

	
	public String streetName() { return _streetName;}

	
	public String streetNameForAddressing() { return _streetNameForAdressing; }

	
	public String suite() {	return _suite;}

	
	public String note() { return _note; }

	
	public String municipalityCode() { return _municipalityCode; }

	
	public String streetBuildingIdentifier() { return _streetBuildingIdentifier;	}

	
	public String streetCode() { return _streetCode; }

	
	public String danishNote() { return _danishNote; }

	
	public String politiDistrikt() { return _politiDistrikt; }

	
	public String postDistrikt() { return _postDistrikt;	}

	
	public String skoleDistrikt() {	return _skoleDistrikt; }

	
	public String socialDistrikt() { return _socialDistrikt; }

	
	public String sogneDistrikt() { return _sogneDistrikt; }

	
	public String valgkredsDistrikt() { return _valgkredsDistrikt; }

	
	public Boolean isSpecielVejkode() {	return _isSpecielVejkode; }

	
	public Boolean isUkendtAdresse() { return _isUkendtAdresse;	}

}

}