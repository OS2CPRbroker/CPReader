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
using System.Collections.Generic;
namespace util.cprbroker.models
{




public class GreenlandicAddress : IGreenlandicAddress {

	public String _countryIdentificationCode;
	public String _districtName;
	public String _districtSubdivisionIdentifier;
	public String _floorIdentifier;
	public String _greenlandBuildingIdentifier;
	public String _mailDeliverySublocationIdentifier;
	public String _municipalityCode;
	public String _postCodeIdentifier;
	public String _streetBuildingIdentifier;
	public String _streetCode;
	public String _streetName;
	public String _streetNameForAddressingName;
	public String _suiteIdentifier;
	public Boolean _isSpecielVejkodeIndikator;
	public Boolean _isUkendtAdresseIndikator;
	public String _note;


	public class Builder : IBuilder<IGreenlandicAddress> {
		
		public String _countryIdentificationCode;
		public String _districtName;
		public String _districtSubdivisionIdentifier;
		public String _floorIdentifier;
		public String _greenlandBuildingIdentifier;
		public String _mailDeliverySublocationIdentifier;
		public String _municipalityCode;
		public String _postCodeIdentifier;
		public String _streetBuildingIdentifier;
		public String _streetCode;
		public String _streetName;
		public String _streetNameForAddressingName;
		public String _suiteIdentifier;
		public Boolean _isSpecielVejkodeIndikator;
		public Boolean _isUkendtAdresseIndikator;
		public String _note;
		
		public IGreenlandicAddress build() { return new GreenlandicAddress(this); }
		
		public Builder countryIdentificationCode(String newCode) { _countryIdentificationCode = newCode; return this; }
		public Builder districtName(String newName) { _districtName = newName; return this; }
		public Builder districtSubdivision(String newIdentifier) { _districtSubdivisionIdentifier = newIdentifier; return this; }
		public Builder floor(String newIdentifier) { _floorIdentifier = newIdentifier.TrimZerosOnLeft(); return this; }
		public Builder greenlandBuilding(String newIdentifier) { _greenlandBuildingIdentifier = newIdentifier.TrimZerosOnLeft(); return this; }
		public Builder mailDeliverySublocation(String newIdentifier) { _mailDeliverySublocationIdentifier = newIdentifier; return this; }
		public Builder municipalityCode(String newCode) { _municipalityCode = newCode.TrimZerosOnLeft(); return this; }
		public Builder postCode(String newIdentifier) { _postCodeIdentifier = newIdentifier.TrimZerosOnLeft(); return this; }
		public Builder streetBuilding(String newIdentifier) { _streetBuildingIdentifier = newIdentifier.TrimZerosOnLeft(); return this; }
		public Builder streetCode(String newCode) { _streetCode = newCode.TrimZerosOnLeft(); return this; }
		public Builder streetName(String newName) { _streetName = newName; return this; }
		public Builder streetNameForAddressing(String newName) { _streetNameForAddressingName = newName; return this; }
		public Builder suite(String newIdentifier) { _suiteIdentifier = newIdentifier.TrimZerosOnLeft(); return this; }
		public Builder isSpecielVejkode(Boolean isSpecial) { _isSpecielVejkodeIndikator = isSpecial; return this; }
		public Builder isUkendtAdresse(Boolean isUkendt) { _isUkendtAdresseIndikator = isUkendt; return this; }
		public Builder note(String newNote) { _note = newNote; return this; }
		
	}
	
	private GreenlandicAddress(Builder builder) {
		
		_countryIdentificationCode = builder._countryIdentificationCode;
		_districtName = builder._districtName;
		_districtSubdivisionIdentifier = builder._districtSubdivisionIdentifier;
		_floorIdentifier = StringUtils.TrimZerosOnLeft(builder._floorIdentifier);
		_greenlandBuildingIdentifier = StringUtils.TrimZerosOnLeft(builder._greenlandBuildingIdentifier);
		_mailDeliverySublocationIdentifier = builder._mailDeliverySublocationIdentifier;
		_municipalityCode = StringUtils.TrimZerosOnLeft(builder._municipalityCode);
		_postCodeIdentifier = builder._postCodeIdentifier;
		_streetBuildingIdentifier = StringUtils.TrimZerosOnLeft(builder._streetBuildingIdentifier);
		_streetCode = StringUtils.TrimZerosOnLeft(builder._streetCode);
		_streetName = builder._streetName;
		_streetNameForAddressingName = builder._streetNameForAddressingName;
		_suiteIdentifier = StringUtils.TrimZerosOnLeft(builder._suiteIdentifier);
		_isSpecielVejkodeIndikator = builder._isSpecielVejkodeIndikator;
		_isUkendtAdresseIndikator = builder._isUkendtAdresseIndikator;
		_note = builder._note;
	}
	
	
	public EAddressType addressType() {	return EAddressType.Greenlandic;}

	
	public IDanishAddress danishAddress() { throw new InvalidOperationException(); }

	
	public IGreenlandicAddress greenlandicAddress() { return this; }

	
	public IWorldAddress worldAddress() { throw new InvalidOperationException(); }

	
	public String note() { return _note;	}

	
	public String countryIdentificationCode() { return _countryIdentificationCode; }

	
	public String districtName() { return _districtName;	}

	
	public String districtSubdivision() {	return _districtSubdivisionIdentifier; }

	
	public String floor() { return _floorIdentifier; }

	
	public String greenlandBuilding() { return _greenlandBuildingIdentifier; }

	
	public String mailDeliverySublocation() {	return _mailDeliverySublocationIdentifier; }

	
	public String municipalityCode() { return _municipalityCode; }

	
	public String postCode() { return _postCodeIdentifier; }

	
	public String streetBuilding() { return _streetBuildingIdentifier; }

	
	public String streetCode() { return _streetCode; }

	
	public String streetName() { return _streetName; }

	
	public String streetNameForAddressing() { return _streetNameForAddressingName; }

	
	public String suite() { return _suiteIdentifier; }

	
	public Boolean isSpecielVejkode() { return _isSpecielVejkodeIndikator; }

	
	public Boolean isUkendtAdresse() { return _isUkendtAdresseIndikator; }

}

}