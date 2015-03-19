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

package util.auth;

import play.mvc.Http;
import util.StringUtils;

import java.util.ArrayList;

/**
 * Created by Beemen on 17/12/2014.
 */
public class WindowsAuthenticationStrategy implements IAuthentication ,IIntegratedAuthenticaton, IGroupAuthentication {
    @Override
    public IAuthenticationResponse authentication(String username, String password) {
        return new AuthenticationResponse(AuthResponseType.SUCCESS, "OK");
    }

    @Override
    public String getUsername(){
        String ret = null;

        Http.Context ctx = Http.Context.current();
        Http.Cookie cookie = ctx.request().cookie("username");

        if(cookie != null) {
            ret = cookie.value();
            ctx.session().put("username", ret);
        }
        return ret;
    }

    @Override
    public String[] getUserGroups(String userName){
        String[] ret = new String[0];

        Http.Context ctx = Http.Context.current();
        Http.Cookie cookie = ctx.request().cookie("usergroups");

        if(cookie != null) {
            String s = cookie.value();
            ret = s.split(",");
            //ctx.session().put("username", ret);
        }
        return ret;
    }

}
