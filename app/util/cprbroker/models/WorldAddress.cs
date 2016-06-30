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







public class WorldAddress : IWorldAddress {

	public String _note;
	public String _countryIdentificationCode;
	public String _locationDescriptionText;
	public String _postalAddressFirstLineText;
	public String _postalAddressSecondLineText;
	public String _postalAddressThirdLineText;
	public String _postalAddressFourthLineText;
	public String _postalAddressFifthLineText;
	public Boolean _isUkendtAdresseIndikator;

	public class Builder : IBuilder<IWorldAddress>{

		public String _note;
		public String _countryIdentificationCode;
		public String _locationDescriptionText;
		public String _postalAddressFirstLineText;
		public String _postalAddressSecondLineText;
		public String _postalAddressThirdLineText;
		public String _postalAddressFourthLineText;
		public String _postalAddressFifthLineText;
		public Boolean _isUkendtAdresseIndikator;
		
		public IWorldAddress build() {return new WorldAddress(this); }

		public Builder note(String newNote) {_note = newNote; return this; }
		public Builder countryIdentificationCode(String newNote) {_countryIdentificationCode = newNote; return this; }
		public Builder locationDescriptionText(String newText) {_locationDescriptionText = newText; return this; }
		public Builder postalAddressFirstLineText(String newLine) {_postalAddressFirstLineText = newLine; return this; }
		public Builder postalAddressSecondLineText(String newLine) {_postalAddressSecondLineText = newLine; return this; }
		public Builder postalAddressThirdLineText(String newLine) {_postalAddressThirdLineText = newLine; return this; }
		public Builder postalAddressFourthLineText(String newLine) {_postalAddressFourthLineText = newLine; return this; }
		public Builder postalAddressFifthLineText(String newLine) {_postalAddressFifthLineText = newLine; return this; }
		public Builder isUkendtAdresseIndikator(Boolean isUkendtAdresse) {_isUkendtAdresseIndikator = isUkendtAdresse; return this; }
	}
	
	private WorldAddress(Builder builder) {

		_note = builder._note;
		_countryIdentificationCode = builder._countryIdentificationCode;
		_locationDescriptionText = builder._locationDescriptionText;
		_postalAddressFirstLineText = builder._postalAddressFirstLineText;
		_postalAddressSecondLineText = builder._postalAddressSecondLineText;
		_postalAddressThirdLineText = builder._postalAddressThirdLineText;
		_postalAddressFourthLineText = builder._postalAddressFourthLineText;
		_postalAddressFifthLineText = builder._postalAddressFifthLineText;
		_isUkendtAdresseIndikator = builder._isUkendtAdresseIndikator;

	}
	
	
	public EAddressType addressType() {	return EAddressType.World; }

	
	public IDanishAddress danishAddress() {	throw new InvalidOperationException(); }

	
	public IGreenlandicAddress greenlandicAddress() { throw new InvalidOperationException(); }

	
	public IWorldAddress worldAddress() { return this; }

	
	public String note() { return _note;	}

	
	public String countryIdentificationCode() {	return _countryIdentificationCode; }

	
	public String locationDescriptionText() { return _locationDescriptionText; }

	
	public String postalAddressFirstLineText() { return _postalAddressFirstLineText; }

	
	public String postalAddressSecondLineText() { return _postalAddressSecondLineText; }

	
	public String postalAddressThirdLineText() { return _postalAddressThirdLineText; }

	
	public String postalAddressFourthLineText() { return _postalAddressFourthLineText; }

	
	public String postalAddressFifthLineText() { return _postalAddressFifthLineText; }

	
	public Boolean isUkendtAdresseIndikator() {	return _isUkendtAdresseIndikator; }

}

}