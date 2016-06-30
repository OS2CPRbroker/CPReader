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




public class CprCitizenData : ICprCitizenRegisterInformation {

	public Boolean _isMemberOfTheChurch;
	public Boolean _isResearcherProtected;
	public Boolean _isSocialSecurityNumberValid;
	public Boolean _isNameAdressProtected;
	public Boolean _isPhoneNumberProtected;
	public String _personNationalityCode;
	public String _socialSecurityNumber;
	public IVirkning _virkning;
	
	public class Builder {

		//Optional parameters - initialized to default values
		public Boolean _isMemberOfTheChurch;
		public Boolean _isResearcherProtected;
		public Boolean _isSocialSecurityNumberValid;
		public Boolean _isNameAdressProtected;
		public Boolean _isPhoneNumberProtected;
		
		public String _personNationalityCode;
		public String _socialSecurityNumber;
		public IVirkning _virkning;
		
		// build method
		public ICprCitizenRegisterInformation build() { return new CprCitizenData(this); }
		
		// builder methods
		public Builder isMemberOfTheChurch(Boolean newBoolean) { _isMemberOfTheChurch = newBoolean; return this; }
		public Builder isResearcherProtected(Boolean newBoolean) { _isResearcherProtected = newBoolean; return this; }
		public Builder isSocialSecurityNumberValid(Boolean newBoolean) { _isSocialSecurityNumberValid = newBoolean; return this; }
		public Builder isNameAdressProtected(Boolean newBoolean) { _isNameAdressProtected = newBoolean; return this; }
		public Builder isPhoneNumberProtected(Boolean newBoolean) { _isPhoneNumberProtected = newBoolean; return this; }
		//TODO This is a 4 digit number not a string..
		public Builder personNationalityCode(String newNationalityCode) { _personNationalityCode = newNationalityCode; return this; }
		//TODO This is a 10 digitnumber, where the 6 first digits are a valid date - not a string!
		public Builder socialSecurityNumber(String newSocialSecurity) { _socialSecurityNumber = newSocialSecurity; return this; }
		public Builder virkning(IVirkning newVirkning) { _virkning = newVirkning; return this; }

	}
	
	private CprCitizenData(Builder builder) {
		_isMemberOfTheChurch = builder._isMemberOfTheChurch;
		_isResearcherProtected = builder._isResearcherProtected;
		_isSocialSecurityNumberValid = builder._isSocialSecurityNumberValid;
		_isNameAdressProtected = builder._isNameAdressProtected;
		_isPhoneNumberProtected = builder._isPhoneNumberProtected;
		_personNationalityCode = builder._personNationalityCode;
		_socialSecurityNumber = builder._socialSecurityNumber;
		_virkning = builder._virkning;
	}
	
	
	public Boolean isMemberOfTheChurch() { return _isMemberOfTheChurch; }

	
	public Boolean isResearcherProtected() { return _isResearcherProtected; }

	
	public Boolean isSocialSecurityNumberValid() { return _isSocialSecurityNumberValid; }

	
	public Boolean isNameAddressProtected() { return _isNameAdressProtected; }

	
	public Boolean isPhoneNumberProtected() { return _isPhoneNumberProtected;	}

	
	public String personNationalityCode() {	return _personNationalityCode; }

	
	public String socialSecurityNumber() { return _socialSecurityNumber;	}

	
	public ICprCitizenRegisterInformation cprCitizen() {
		return this;
	}

	
	public IVirkning virkning() { return _virkning; }

}

}