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

using System.Web.Mvc;
using util.cprbroker;
using System;
using System.Net;
using System.Linq;
using System.Collections.Generic;
using util;

namespace controllers
{

    public class Search : Controller
    {

        private static ICprBrokerAccessor cprBroker;
        private static Boolean onlineCacheEnabled;
        private static int onlineCacheTimeout;
        //public static IConfiguration config;

        public Search(ICprBrokerAccessor newCprBroker)
        {
            cprBroker = newCprBroker;
            onlineCacheEnabled = cpreader.Properties.Settings.Default.cprbroker_onlinecacheenabled;
            onlineCacheTimeout = cpreader.Properties.Settings.Default.cprbroker_onlinecacheseconds;
        }

        public static String getSessionId()
        {
            // Generate a unique id
            var session = System.Web.HttpContext.Current.Session;
            String uuid = session["uuid"] as string;
            if (uuid == null)
            {
                uuid = Guid.NewGuid().ToString();
                session["uuid"] = uuid;
            }
            return uuid;
        }

        public ActionResult searchNameAndAddress(String name, String address, bool online, int page)
        {
            List<IPerson> persons = null;
            try
            {

                // log what page the user requested
                play.Logger.info(String.Format("At <{0}> user <{1}> searched for name<{2}>, address<{3}>; online <{4}>, page <{5}>",
                        DateTime.Now,
                        User.Identity.Name,
                        name,
                        address,
                        online,
                        page
                ));

                String key = String.Format("session={0};name={1};address={2}", getSessionId(), name, address);
                // TODO: See if there is a cahce equivalent in ASP.NET
                /*if (online && onlineCacheEnabled)
                {
                    Object o = Cache.get(key);
                    if (o != null)
                        persons = (List<IPerson>)o;
                }*/
                if (persons == null)
                {
                    persons = cprBroker.searchList(
                            name,
                            address,
                            online ? ESourceUsageOrder.ExternalOnly : ESourceUsageOrder.LocalOnly,
                            -1, -1);
                }
                // TODO: See if there is a cahce equivalent in ASP.NET
                /*
                if (online && onlineCacheEnabled)
                {
                    // Temporarily store the results for a while
                    Cache.set(key, persons, onlineCacheTimeout);
                }*/
            }
            catch (Exception ex)
            {
                play.Logger.error(ex);
            }


            String path = Request.Path;
            path = path.Substring(0, path.IndexOf("page") + 5);

            SearchInput searchInput = new SearchInput(name, address, online);
            searchInput.saveToSession(this);

            int accessLevel = AccessLevelManager.getCurrentAccessLevel();
            if (persons != null)
            {
                // calculate the searchIndex, which is the starting point of the search
                int fromIndex = ((page - 1) * 10);
                int toIndex = ((page) * 10);

                if (persons.Count < fromIndex)
                    return new HttpStatusCodeResult(HttpStatusCode.BadRequest);

                if (persons.Count < toIndex)
                    toIndex = persons.Count;

                List<IPerson> subPersons = persons.Take(toIndex).Skip(fromIndex).ToList();

                return View(new Tuple<List<IPerson>, int, int, string, SearchInput, int>(subPersons, persons.Count, page, path, searchInput, accessLevel));
            }
            else
            {
                return View(new Tuple<List<IPerson>, int, int, string, SearchInput, int>(persons, 1, page, path, searchInput, accessLevel));
            }
        }

        /**
         * @param uuid String with the uuid of a person
         * @return Result containing the response from the cprBroker
         */
        public ActionResult showPerson(String uuid)
        {
            // Logging the show request
            play.Logger.info(String.Format("At <{0}> user <{1}> requested to see uuid <{2}>",
                    DateTime.Now,
                    User.Identity.Name,
                    uuid
            ));

            IPerson person = null;
            try
            {
                person = cprBroker.read(uuid);

                // Logging the show request
                play.Logger.info(User.Identity.Name + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
            }
            catch (Exception ex)
            {
                play.Logger.error(ex);
            }


            SearchInput searchInput = new SearchInput();
            searchInput.saveToSession(this);
            //searchInput.fillFromSession(this);

            // access level - add to person model
            if (person == null)
            {
                return View("show_error", new Tuple<int, SearchInput>(503, searchInput));
            }
            if (person.code() == 200)
            {
                List<IPerson> persons = new List<IPerson>();
                persons.Add(person);
                String path = Request.Path;
                path = path.Substring(0, path.IndexOf("page") + 5);
                int page = 1;
                int accessLevel = AccessLevelManager.getCurrentAccessLevel();

                return View("list", new Tuple<List<IPerson>, int, int, string, SearchInput, int>(
                    persons, 1, page, path, searchInput, accessLevel));
            }
            else {
                //TODO - A person wasn't found
                return View("show_error", new Tuple<int, SearchInput>(person.code(), searchInput));
            }
        }
        /*
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
            Integer accessLevel = AccessLevelManager.getCurrentAccessLevel();

            if (accessLevel < 1)
            {
                return ok(views.html.access_denied.render(401, searchInput, "Access denied."));
            }
            if (person == null) {
                return ok(show_error.render(503, searchInput));
            }


            if (person.code() == 200) { 
                    return ok(views.html.person.render(person, searchInput, accessLevel));   

            } else {
                //TODO - A person wasn't found
                return ok(show_error.render(person.code(), searchInput));
            }
        }
        */

        public ActionResult getUuidFromCpr(SearchInput searchForm)
        {
            //Form<SearchInput> searchForm = Form.form(SearchInput.class).bindFromRequest();

            // Search is now by CPR, clear the saved name (if any)
            SearchInput searchInput = new SearchInput();
            searchInput.fillFromSession(this);
            searchInput.setQuery("");
            searchInput.saveToSession(this);

            // Logging the search
            play.Logger.info(User.Identity.Name + " searched for: " + searchForm.query);

            // Check if there is errors (empty strings)
            // TODO: Check the ASP.NET euivalent
            /*if (searchForm.hasErrors())
            {
                return badRequest("Form had errors");
            }*/

            // Input type == cprnumber
            IUuid uuid = cprBroker.getUuid(searchForm.query);

            // logging the returned resultcode
            play.Logger.info(User.Identity.Name + "'s search request to CPRBroker responded, " + uuid.code() + " - " + uuid.message());

            if (uuid.code() == 200)
            {
                String uuidStr = uuid.value();
                return Content(uuidStr);
            }
            else {
                // this should never happen as person master will just assign
                // a new uuid if it doesn't exist
                play.Logger.info("search form has errors");
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest, "CPR not found in local");
            }
        }

        public ActionResult updateParents(String uuid)
        {
            play.Logger.info("UPDATE PARENTS: " + uuid);

            IPerson person = null;
            try
            {
                person = cprBroker.read(uuid);

                // Logging the show request
                play.Logger.info(User.Identity.Name + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
            }
            catch (Exception ex)
            {
                play.Logger.error(ex);
            }
            SearchInput searchInput = new SearchInput();
            searchInput.fillFromSession(this);

            // access level - add to person model
            int accessLevel = AccessLevelManager.getCurrentAccessLevel();

            if (accessLevel < 1)
            {
                return View("access_denied", new Tuple<int, SearchInput, string>(401, searchInput, "Access denied."));
            }
            if (person == null)
            {
                return View("show_error", new Tuple<int, SearchInput>(503, searchInput));
            }


            if (person.code() == 200)
            {
                return View("parentlist", new Tuple<IPerson, int>(person, accessLevel));
            }
            else {
                //TODO - A person wasn't found
                return View("show_error", new Tuple<int, SearchInput>(person.code(), searchInput));
            }
        }

        public ActionResult updatePerson(String uuid)
        {
            play.Logger.info("UPDATE person: " + uuid);

            IPerson person = null;
            try
            {
                person = cprBroker.read(uuid);

                // Logging the show request
                play.Logger.info(User.Identity.Name + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
            }
            catch (Exception ex)
            {
                play.Logger.error(ex);
            }
            SearchInput searchInput = new SearchInput();
            searchInput.fillFromSession(this);

            // access level - add to person model
            int accessLevel = AccessLevelManager.getCurrentAccessLevel();

            if (accessLevel < 1)
            {
                return View("access_denied", new Tuple<int, SearchInput, string>(401, searchInput, "Access denied."));
            }
            if (person == null)
            {
                return View("show_error", new Tuple<int, SearchInput>(503, searchInput));
            }

            if (person.code() == 200)
            {
                return View("modalcontent", new Tuple<IPerson, int>(person, accessLevel));

            }
            else {
                //TODO - A person wasn't found
                return View("show_error", new Tuple<int, SearchInput>(person.code(), searchInput));
            }
        }


        public ActionResult closeDetail()
        {
            play.Logger.info("CLOSE DETAIL ");
            this.Session["showperson"] = "none";
            return Content("Closed");
        }

        public class SearchInput
        {

            public SearchInput()
            {
                this.setQuery("");
                this.setAddressQuery("");
                this.setOnlineFromConfigOrValue(false);
            }

            public SearchInput(String name, String address, Boolean online)
            {
                this.setQuery(name);
                this.setAddressQuery(address);
                this.setOnlineFromConfigOrValue(online);
            }

            public String query;

            public String getQuery()
            {
                return query;
            }

            public void setQuery(String query)
            {
                this.query = query;
            }

            public String addressQuery;

            public String getAddressQuery()
            {
                return addressQuery;
            }

            public void setAddressQuery(String query)
            {
                this.addressQuery = query;
            }

            public bool online;

            public bool getOnline()
            {
                return this.online;
            }

            public void setOnline(bool value)
            {
                this.online = value;
            }

            public void setOnlineFromConfigOrValue(bool value)
            {
                if (cpreader.Properties.Settings.Default.search_type == 1)
                {
                    play.Logger.info("OFFLINE SEARCH");
                    this.setOnline(false); // local
                }
                else if (cpreader.Properties.Settings.Default.search_type == 2)
                {
                    play.Logger.info("ONLINE SEARCH");
                    this.setOnline(true); // online
                }
                else
                {
                    play.Logger.info("USE RADIO BUTTON VALUE: " + online);
                    this.setOnline(value); // use radio button value
                }
            }

            public void fillFromSession(Controller controller)
            {
                setQuery(StringUtils.format("{0}", controller.Session["query"]));
                setAddressQuery(StringUtils.format("{0}", controller.Session["addressQuery"]));


                if (cpreader.Properties.Settings.Default.search_type == 1)
                {
                    play.Logger.info("OFFLINE SEARCH");
                    this.setOnline(false); // local            
                }
                else if (cpreader.Properties.Settings.Default.search_type == 2)
                {
                    play.Logger.info("ONLINE SEARCH");
                    this.setOnline(true); // online
                }
                else
                {
                    String onlineS = controller.Session["online"] as string;
                    if ("true".Equals(onlineS))
                        setOnline(true);
                }

                setOnline(true);
            }

            public void saveToSession(Controller controller)
            {
                controller.Session["query"] = getQuery();
                controller.Session["addressQuery"] = getAddressQuery();
                String onlineS = "false";
                if (getOnline())
                    onlineS = "true";
                controller.Session["online"] = onlineS;
            }

        }

    }
}