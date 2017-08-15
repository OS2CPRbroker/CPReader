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
using System.Text.RegularExpressions;

namespace util.cprbroker.models
{
    public class Uuid : IUuid
        {
	
	    public String _uuid;
	    public String _message;
	    public int _code;
	
	    // Lazy initialized, cached hashCode
	    public int _hashCode;
	
	    /**
	     * 
	     * @param newUuid String representation of a hyphenated Guid with a length of 36
	     * @param newCode CPR Broker status code
	     * @param newMessage CPR Broker message
	     * @throws IllegalArgumentException Throws if newUuid does not match uuid regex (with or without curly brackets)
	     */
	    public Uuid(String newUuid, int newCode, String newMessage)
        {
            if (String.IsNullOrEmpty(newUuid))
                newUuid = "";

            if (!Regex.IsMatch(newUuid, cpreader.app.util.Constants.uuidRegex)) throw new ArgumentException("A uuid must be a String representation of a hyphenated Guid with a length of 36");
		    _uuid = newUuid;
		    _message = newMessage;
		    _code = newCode;
	    }

	
	    public String message() {
		    return _message;
	    }

	
	    public int code() {
		    return _code;
	    }

	
	    public String value() {
		    return _uuid;
	    }
	
	
	    public String toString() {
		    return _uuid;
	    }
	
	
	


    }

}