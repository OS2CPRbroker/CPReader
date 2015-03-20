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

package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.mvc.*;
import play.mvc.Controller;
import play.mvc.Result;
import util.auth.AuthResponseType;
import util.auth.IAuthenticationResponse;
import util.auth.Secured;
import views.html.login;
import play.cache.Cache;


import conf.IConfiguration;
import play.Configuration;
import java.io.InputStream;
import java.lang.ClassLoader;
import play.api.Play;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import play.Application.*;
import play.api.libs.json.*;


/**
 * @author Beemen Beshara
 * @author Søren Kirkegård
 */
@Singleton
public class Signon extends Controller {

    public static class Login {

        @Required
        public String username;

        @Required
        public String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static Result login() {
        return ok(
                login.render(Form.form(Login.class))
        );
    }

    public void initGroups()
    {
        String groupslist = "group0, anothergroup, andanothergroup";
        session("grouplist", groupslist);
    }

    public String getAccessLevel(String username)
    {
        initGroups(); // test data

        // parse csv access levels file
        String accessFileURL = play.Play.application().configuration().getString("accessfile.url")+play.Play.application().configuration().getString("accessfile.name");
        String accesslevel = "0";
        InputStream inputStream = null;
        BufferedReader fileReader = null;

        final String DELIMITER = ",";


        boolean onlineFile=false; // just use a local file in the 'conf' directory for now
        if (play.Play.application().configuration().getString("accessfile.online").equals("true"))
        {
            onlineFile=true;
        }

        try
        {
            if(onlineFile) 
            {
                try
                {
                    inputStream = new URL(accessFileURL).openStream(); // online
                }
                catch(Exception e)
                {
                    play.Logger.info("FAILED TO ONLINE OPEN ACCESS FILE, DEFAULTING TO LOCAL FILE");
                    inputStream = play.Play.application().resourceAsStream(play.Play.application().configuration().getString("accessfile.name")); // local (stored in 'conf' directory)
                }

            }
            else 
            {
                inputStream = play.Play.application().resourceAsStream(play.Play.application().configuration().getString("accessfile.name")); // local (stored in 'conf' directory)
            }

            fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             
            String line = "";
            String grouplist = session("grouplist");
            String[] groupnames = grouplist.split(DELIMITER);

            while ((line = fileReader.readLine()) != null)
            {   
                if (line.isEmpty() || line.trim().equals("") || line.trim().equals("\n"))
                {
                    // skip it
                }
                else
                {
                    //Get all tokens available in line
                    String[] tokens = line.split(DELIMITER);
                   
                    // check group level access
                    if (groupnames != null && groupnames.length > 0)
                    {
                        for (int i = 0;  i < groupnames.length; i++)
                        {
                            groupnames[i] = groupnames[i].replaceAll("\\s",""); // strip any white space
                            if (tokens[0].toLowerCase().equals(groupnames[i].toLowerCase()))
                            {
                                // we have a match, so return the access level
                                accesslevel = tokens[1];
                                // set user cart usage
                                if (play.Play.application().configuration().getString("cart.enabled").equals("true"))
                                {
                                    // user specific cart access
                                    session("usecart", ""+tokens[2]);
                                }
                            }
                        }
                    }

                    // check individual user level access (overrides group level access)
                    if (tokens[0].toLowerCase().equals(username.toLowerCase()))
                    {
                        // we have a match, so return the access level
                        accesslevel = tokens[1];

                        // set user cart usage
                        if (play.Play.application().configuration().getString("cart.enabled").equals("true"))
                        {
                            // user specific cart access
                            session("usecart", ""+tokens[2]);
                        }   
                    }
                }
            }
            // override any cart access if the master cart usage is set to false
            if (play.Play.application().configuration().getString("cart.enabled").equals("false"))
            {
                // disable for all
                session("usecart", "0");  
            }
        }
        catch (Exception e) {
            play.Logger.info("ERROR ");
        }
        finally
        {
            try {
                fileReader.close();
            } catch (IOException e) {
                play.Logger.info("ERROR");
            }
        }

        return accesslevel; // lowest level access as default
    }



    public Result authenticate() {

        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        // Check if there is errors (empty strings)
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }

        IAuthenticationResponse authResponse = Secured.authenticationStrategy.authentication(
                loginForm.get().username,
                loginForm.get().password);

        if (authResponse.type().equals(AuthResponseType.SUCCESS)) {
            session().clear();
            session("username", loginForm.get().username);

            // save the access level here
            //session("accesslevel", getAccessLevelFromFile(loginForm.get().username, loginForm.get().password));


            //Cache.set("accesslevel", getAccessLevel(loginForm.get().username), 3600); // good for 2 hours
            Cache.set("accesslevel", 0, 0);
            Cache.set("accesslevel", getAccessLevel(loginForm.get().username), 3600); // good for 2 hours

            play.Logger.info("[" + request().remoteAddress() + "] " +
                    session("username") +
                    " has succesfully logged in.");

            return redirect(
                    routes.Home.index()
            );
        } else {
            List<ValidationError> errors =
                    new ArrayList<ValidationError>();
            ValidationError error =
                    new ValidationError(authResponse.type().toString(),
                            play.i18n.Messages.get(authResponse.message()));
            errors.add(error);
            loginForm.errors().put("error", errors);
            play.Logger.debug(error.toString());
            // logging the login attempt
            play.Logger.info("[" + request().remoteAddress() + "] " +
                    "Login attempt with bad credientials.");

            // clear the cart contents
            Cache.set("numcartitems", 0, 0);
            Cache.set("cartdata", null, 0);

            return badRequest(login.render(loginForm));
        }
    }

    public static Result logout() {

        // logging that a user has logged out
        play.Logger.info("[" + request().remoteAddress() + "] " +
                session("username") +
                " has succesfully logged out.");

        session().clear();

        // clear the cart contents on logout
        Cache.set("numcartitems", 0, 0);
        Cache.set("cartdata", null, 0);

        
        //flash("success", play.i18n.Messages.get("logout.succesful"));
        return redirect(
                routes.Signon.login()
        );
    }
}
