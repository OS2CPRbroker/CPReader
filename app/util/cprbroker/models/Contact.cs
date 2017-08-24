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



public class Contact : IContact {
	
	public String _limitedUsageText;
	public String _email;
	public String _noteText;
	public String _phone;
	public String _otherContactText;
	public String _otherContactNoteText;
	public Boolean _isPhoneAbleToRecieveSms;
	
	public class Builder {
	
		public Boolean _isPhoneAbleToRecieveSms;
		public String _limitedUsageText;
		public String _email;
		public String _noteText;
		public String _phone;
		public String _otherContactText;
		public String _otherContactNoteText;
		
		public IContact build() { return new Contact(this); }
		
		public Builder limitedUsageText(String newText) {_limitedUsageText = newText; return this;}
		public Builder email(String newEmail) {_email = newEmail; return this;}
		public Builder noteText(String newText) {_noteText = newText; return this;}
		public Builder phone(String newPhone) {_phone = newPhone; return this;}
		public Builder otherContactText(String newText) {_otherContactText = newText; return this;}
		public Builder otherContactNoteText(String newText) {_otherContactNoteText = newText; return this;}
		public Builder isPhoneAbleToRecieveSms(Boolean isAble) {_isPhoneAbleToRecieveSms = isAble; return this; }
	}
	
	private Contact(Builder builder) {
		_limitedUsageText = builder._limitedUsageText;
		_email = builder._email;
		_noteText = builder._noteText;
		_phone = builder._phone;
		_otherContactText = builder._otherContactText;
		_otherContactNoteText = builder._otherContactNoteText;
		_isPhoneAbleToRecieveSms = builder._isPhoneAbleToRecieveSms;
	}

	
	public String limitedUsageText() { return _limitedUsageText; }

	
	public String email() { return _email; }

	
	public String noteText() { return _noteText;	}

	
	public String phone() { return _phone; }

	
	public String otherContactText() { return _otherContactText;	}

	
	public String otherContactNoteText() { return _otherContactNoteText;	}

	
	public Boolean isPhoneAbleToRecieveSms() { return _isPhoneAbleToRecieveSms; }

}

}