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

import conf.IConfiguration;
import org.joda.time.DateTime;
import play.cache.Cache;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.StringUtils;
import util.auth.Secured;
import util.cprbroker.ESourceUsageOrder;
import util.cprbroker.ICprBrokerAccessor;
import util.cprbroker.IPerson;
import util.cprbroker.IUuid;
import views.html.list;
import views.html.show_error;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

@Singleton
public class Search extends Controller {

    private static ICprBrokerAccessor cprBroker;
    private static int onlineCacheTimeout;

    @Inject
    public Search(ICprBrokerAccessor newCprBroker, IConfiguration conf)
    {
        cprBroker = newCprBroker;
        onlineCacheTimeout = conf.getConfiguration().getInt("cprbroker.onlinecacheseconds");
    }

    String getSessionId() {
        // Generate a unique id
        String uuid = session("uuid");
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
            session("uuid", uuid);
        }
        return uuid;
    }

    @Security.Authenticated(Secured.class)
    public Result searchNameAndAddress(String name, String address, boolean online, int page) {

        List<IPerson> persons = null;
        try {

            // log what page the user requested
            play.Logger.info(String.format("At <%s> user <%s> searched for name<%s>, address<%s>; online <%b>, page <%d>",
                    DateTime.now(),
                    Secured.getCurrntUsername(),
                    name,
                    address,
                    online,
                    page
            ));

            String key = String.format("session=%s;name=%s;address=%s", getSessionId(), name, address);
            if (online) {
                Object o = Cache.get(key);
                if (o != null)
                    persons = (List<IPerson>) o;
            }
            if (persons == null) {
                persons = cprBroker.searchList(
                        name,
                        address,
                        online ? ESourceUsageOrder.ExternalOnly : ESourceUsageOrder.LocalOnly,
                        -1, -1);
            }
            if (online) {
                // Temporarily store the results for a while
                Cache.set(key, persons, onlineCacheTimeout);
            }
        } catch (Exception ex) {
            play.Logger.error(ex.toString());
        }


        String path = request().path();
        path = path.substring(0, path.indexOf("page") + 5);

        SearchInput searchInput = new SearchInput(name, address, online);
        searchInput.saveToSession(this);

        if (persons != null) {
            // calculate the searchIndex, which is the starting point of the search
            int fromIndex = ((page - 1) * 10);
            int toIndex = ((page) * 10);

            if (persons.size() < fromIndex)
                return badRequest();

            if (persons.size() < toIndex)
                toIndex = persons.size();

            List<IPerson> subPersons = persons.subList(fromIndex, toIndex);

            return ok(list.render(subPersons, persons.size(), page, path, searchInput));
        } else
            return ok(list.render(persons, 0, page, path, searchInput));
    }

    /**
     * @param uuid String with the uuid of a person
     * @return Result containing the response from the cprBroker
     */
    @Security.Authenticated(Secured.class)
    public Result showPerson(String uuid) {
        // Logging the show request
        play.Logger.info(String.format( "At <%s> user <%s> requested to see uuid <%s>",
                DateTime.now(),
                Secured.getCurrntUsername(),
                uuid
        ));

        IPerson person = null;
        try {
            person = cprBroker.read(uuid);

            // Logging the show request
            play.Logger.info(session("username") + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
        } catch (Exception ex) {
            play.Logger.error(ex.toString());
        }

        SearchInput searchInput = new SearchInput();
        searchInput.fillFromSession(this);

        // access level - add to person model
        Integer accessLevel=0;
        if (Cache.get("accesslevel") != null)
        {
            accessLevel = Integer.parseInt(Cache.get("accesslevel").toString());
        }
        
        // test access level
        //play.Logger.info("Access Level: " + accessLevel);
        //accessLevel = 1;
        //session("accesslevel", ""+accessLevel);
        // end test access level
        if (person == null) {
            return ok(show_error.render(503, searchInput));
        }
        if (person.code() == 200) {  
            List<IPerson> persons = new ArrayList<IPerson>();
            persons.add(person);
            String path = request().path();
            path = path.substring(0, path.indexOf("page") + 5);
            int page = 1;      
            return ok(views.html.list_detailed.render(persons, persons.size(), page, path, searchInput, accessLevel)); 
        } else {
            //TODO - A person wasn't found
            return ok(show_error.render(person.code(), searchInput));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result showPersonFull(String uuid) {
        // Logging the show request
        play.Logger.info(String.format( "At <%s> user <%s> requested to see uuid <%s>",
                DateTime.now(),
                Secured.getCurrntUsername(),
                uuid
        ));

        IPerson person = null;
        try {
            person = cprBroker.read(uuid);

            // Logging the show request
            play.Logger.info(session("username") + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
        } catch (Exception ex) {
            play.Logger.error(ex.toString());
        }

        SearchInput searchInput = new SearchInput();
        searchInput.fillFromSession(this);
        // access level - add to person model
        Integer accessLevel=0;
        if (Cache.get("accesslevel") != null)
        {
            accessLevel = Integer.parseInt(Cache.get("accesslevel").toString());
        }



        if (person == null) {
            return ok(show_error.render(503, searchInput));
        }

        play.Logger.info("1 SHOW PERSON FULL WITH ACCESS LEVEL :"+accessLevel);


        if (person.code() == 200) {
            if (accessLevel < 2) { // never allow full access unless access level is 2
                List<IPerson> persons = new ArrayList<IPerson>();
                persons.add(person);
                String path = request().path();
                path = path.substring(0, path.indexOf("page") + 5);
                int page = 1;      
                play.Logger.info("2 SHOW PERSON FULL WITH ACCESS LEVEL :"+accessLevel);
                return ok(views.html.list_detailed.render(persons, persons.size(), page, path, searchInput, accessLevel)); 
            }
            else {
                play.Logger.info("3 SHOW PERSON FULL WITH ACCESS LEVEL :"+accessLevel);
                return ok(views.html.person.render(person, searchInput, accessLevel));   
            }

              
        } else {
            //TODO - A person wasn't found
            play.Logger.info("4 SHOW PERSON FULL WITH ACCESS LEVEL :"+accessLevel);
            return ok(show_error.render(person.code(), searchInput));
        }
    }


    @Security.Authenticated(Secured.class)
    public Result getUuidFromCpr() {

        Form<SearchInput> searchForm = Form.form(SearchInput.class).bindFromRequest();

        // Search is now by CPR, clear the saved name (if any)
        SearchInput searchInput = new SearchInput();
        searchInput.fillFromSession(this);
        searchInput.setQuery("");
        searchInput.saveToSession(this);

        // Logging the search
        play.Logger.info(session("username") + " searched for: " + searchForm.get().query);

        // Check if there is errors (empty strings)
        if (searchForm.hasErrors()) {
            return badRequest("Form had errors");
        }

        // Input type == cprnumber
        IUuid uuid = cprBroker.getUuid(searchForm.get().query);

        // logging the returned resultcode
        play.Logger.info(session("username") + "'s search request to CPRBroker responded, " + uuid.code() + " - " + uuid.message());

        if (uuid.code() == 200) {
            String uuidStr = uuid.value();
            return ok(uuidStr);
        } else {
            // this should never happen as person master will just assign
            // a new uuid if it doesn't exist
            play.Logger.info("search form has errors");
            return badRequest("CPR not found in local");
        }
    }

    public static class SearchInput {

        public SearchInput() {
            this.setQuery("");
            this.setAddressQuery("");
            this.setOnline(false);
        }

        public SearchInput(String name, String address, Boolean online) {
            this.setQuery(name);
            this.setAddressQuery(address);
            this.setOnline(online);
        }

        @Required
        public String query;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String addressQuery;

        public String getAddressQuery() {
            return addressQuery;
        }

        public void setAddressQuery(String query) {
            this.addressQuery = query;
        }

        public boolean online;

        public boolean getOnline() {
            return this.online;
        }

        public void setOnline(boolean value) {
            this.online = value;
        }

        public void fillFromSession(Controller controller) {
            setQuery(StringUtils.format("%s",controller.session("query")));
            setAddressQuery(StringUtils.format("%s", controller.session("addressQuery")));

            String onlineS = controller.session("online");
            if ("true".equals(onlineS))
                setOnline(true);
        }

        public void saveToSession(Controller controller) {
            controller.session("query", getQuery());
            controller.session("addressQuery", getAddressQuery());
            String onlineS = "false";
            if (getOnline())
                onlineS = "true";
            controller.session("online", onlineS);
        }

    }

}
