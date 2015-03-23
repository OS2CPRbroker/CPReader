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

package util.auth;

import javax.inject.Inject;

import conf.IConfiguration;
import play.Configuration;
/**
 * Simple example implementation of the IAuthentication used for testing
 * @author Søren Kirkegård
 *
 */
public class TestAuthenticationStrategy implements IAuthentication, IGroupAuthentication {

	private final String username;
	private final String password;
	
	/**
	 * Constructor taking a configuration-object and validating it.
	 * @param config
	 */
	@Inject
	public TestAuthenticationStrategy(final IConfiguration configuration) {
		// validation of the configuration object at instantiation
		play.Configuration config = configuration.getConfiguration();
		validate(config);
		
		username = config.getString("auth.username");
		password = config.getString("auth.password");
	}

	/**
	 * Helper method to validate an IAuthStrategy.
	 * Note this SHOULD handle error logging, if the state of the
	 * configuration-object isn't valid and throw an IllegalStateException.
	 * @param config
	 */
	public static void validate(final Configuration config) {
		
		//Validate the configuration object here
		String username = config.getString("auth.username");
		String password = config.getString("auth.password");

		if (username == null) {
			play.Logger.info("TestAuthenticationStrategy.validate(): ERROR auth.username was NULL. Is it correctly setup in config?");
			throw new IllegalStateException("TestAuthenticationStrategy.validate(): auth.username was NULL.");
		}

		if (password == null) {
			play.Logger.info("TestAuthenticationStrategy.validate(): ERROR auth.password was NULL");
			throw new IllegalStateException("TestAuthenticationStrategy.validate(): auth.password was NULL. Is it correctly setup in config?");
		}
		
		play.Logger.debug("TestAuthenticationStrategy.validate(): Done");
		play.Logger.info("TestAuthenticationStrategy validated");
		
	}

	/**
	 * Authentication method
	 */
	@Override
	public IAuthenticationResponse authentication(final String username, final String password) {
		play.Logger.info("User tried to login with: " + username + ", " + password);
		if(username.equals(this.username) && password.equals(this.password)) {
			play.Logger.info("Returning AuthResponse with SUCCESS");
			return new AuthenticationResponse(AuthResponseType.SUCCESS, "OK");
		} else {
			return new AuthenticationResponse(AuthResponseType.ERROR, "Forkert brugernavn eller kodeord");
		}
	}

    @Override
    public String[] getUserGroups(String userName) {
        String[] ret = new String[3];
        ret[0] = "group0";
        ret[1] = "anothergroup";
        ret[2] = "andanothergroup";
        return ret;
    }
}
