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
using System.Web;

namespace cpreader.Controllers
{

    public class SearchController : Controller
    {

        public static ICprBrokerAccessor cprBroker;
        private static Boolean onlineCacheEnabled;
        private static int onlineCacheTimeout;
        //public static IConfiguration config;

        public SearchController()
        {
            cprBroker = new util.cprbroker.jaxws.JaxWsCprBroker();
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
                String hostName = Dns.GetHostName();
                String ipAddress = LoggingTools.GetVisitorIPAddress();

                // log what page the user requested
                cpreader.Logger.info(String.Format("At <{0}> user <{1}> searched for name<{2}>, address<{3}>; online <{4}>, page <{5}>. Host name: <{6}> at local IP address <{7}>.",
                        DateTime.Now,
                        User.Identity.Name,
                        name,
                        address,
                        online,
                        page,
                        hostName,
                        ipAddress
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
                cpreader.Logger.error(ex);
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

                return View("list", new list_viewModel(subPersons, persons.Count, page, path, searchInput, accessLevel));
            }
            else
            {
                return View("list", new list_viewModel(persons, 1, page, path, searchInput, accessLevel));
            }
        }

        /**
         * @param uuid String with the uuid of a person
         * @return Result containing the response from the cprBroker
         */
        public ActionResult showPerson(String uuid)
        {
            String hostName = Dns.GetHostName();
            String ipAddress = LoggingTools.GetVisitorIPAddress();

            // Logging the show request
            cpreader.Logger.info(String.Format("At <{0}> user <{1}> requested to see uuid <{2}>. Host name: <{3}> at local IP address <{4}>.",
                    DateTime.Now,
                    User.Identity.Name,
                    uuid,
                    hostName,
                    ipAddress
            ));

            IPerson person = null;
            try
            {
                person = cprBroker.read(uuid);

                // Logging the show request
                cpreader.Logger.info(String.Format("{0}'s request to CPRBroker responded, {1} - {2}.",
                    User.Identity.Name,
                    person.code(),
                    person.message()
                    ));
            }
            catch (Exception ex)
            {
                cpreader.Logger.error(ex);
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
                int page = 1;
                int accessLevel = AccessLevelManager.getCurrentAccessLevel();

                return View("list", new list_viewModel(
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
            cpreader.Logger.info(String.format( "At <%s> user <%s> requested to see uuid <%s>",
                    DateTime.now(),
                    Secured.getCurrntUsername(),
                    uuid
            ));

            IPerson person = null;
            try {
                person = cprBroker.read(uuid);

                // Logging the show request
                cpreader.Logger.info(session("username") + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
            } catch (Exception ex) {
                cpreader.Logger.error(ex.toString());
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

        [HttpPost]
        public ActionResult getUuidFromCpr(string query)
        {
            //query = System.Web.HttpContext.Current.Request["query"],
            // Search is now by CPR, clear the saved name (if any)
            SearchInput searchInput = new SearchInput();
            searchInput.fillFromSession(this);
            searchInput.setQuery("");
            searchInput.saveToSession(this);

            String hostName = Dns.GetHostName();
            String ipAddress = LoggingTools.GetVisitorIPAddress();

            // Logging the search
            cpreader.Logger.info(String.Format("<{0}> searched for: <{1}> from host name: <{2}> at local IP address <{3}>.", 
                User.Identity.Name,
                query,
                hostName,
                ipAddress
                ));

            // Check if there is errors (empty strings)
            // TODO: Check the ASP.NET euivalent
            /*if (searchForm.hasErrors())
            {
                return badRequest("Form had errors");
            }*/

            // Input type == cprnumber
            IUuid uuid = cprBroker.getUuid(query);

            // logging the returned resultcode
            cpreader.Logger.info(User.Identity.Name + "'s search request to CPRBroker responded, " + uuid.code() + " - " + uuid.message());

            if (uuid.code() == 200)
            {
                String uuidStr = uuid.value();
                return Content(uuidStr);
            }
            else {
                // this should never happen as person master will just assign
                // a new uuid if it doesn't exist
                cpreader.Logger.info(String.Format("CPR number <{0}> not found.", query));
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest, "CPR not found in local");
            }
        }

        public ActionResult updateParents(String uuid)
        {
            cpreader.Logger.info("Accessing parents of person: " + uuid);

            IPerson person = null;
            try
            {
                person = cprBroker.read(uuid);

                // Logging the show request
                cpreader.Logger.info(User.Identity.Name + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
            }
            catch (Exception ex)
            {
                cpreader.Logger.error(ex);
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
                return PartialView("parentlist", new Tuple<IPerson, int>(person, accessLevel));
            }
            else {
                //TODO - A person wasn't found
                return PartialView("show_error", new Tuple<int, SearchInput>(person.code(), searchInput));
            }
        }

        public ActionResult updateFullpage(String uuid)
        {
            cpreader.Logger.info("Accessing overview of person: " + uuid);

            IPerson person = null;
            try
            {
                person = cprBroker.read(uuid);

                // Logging the show request
                cpreader.Logger.info(User.Identity.Name + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
            }
            catch (Exception ex)
            {
                cpreader.Logger.error(ex);
            }
            SearchInput searchInput = new SearchInput();
            searchInput.fillFromSession(this);

            // access level - add to person model
            int accessLevel = AccessLevelManager.getCurrentAccessLevel();

            if (accessLevel < 1)
            {
                return PartialView("access_denied", new Tuple<int, SearchInput, string>(401, searchInput, "Access denied."));
            }
            if (person == null)
            {
                return PartialView("show_error", new Tuple<int, SearchInput>(503, searchInput));
            }

            if (person.code() == 200)
            {
                ActionResult ret = PartialView("fullpagemodalcontent", new Tuple<IPerson, int>(person, accessLevel));
                return ret;

            }
            else
            {
                //TODO - A person wasn't found
                return PartialView("show_error", new Tuple<int, SearchInput>(person.code(), searchInput));
            }
        }
            

        public ActionResult updatePerson(String uuid)
        {
            cpreader.Logger.info("Accessing person: " + uuid);

            IPerson person = null;
            try
            {
                person = cprBroker.read(uuid);

                // Logging the show request
                cpreader.Logger.info(User.Identity.Name + "'s request to CPRBroker responded, " + person.code() + " - " + person.message());
            }
            catch (Exception ex)
            {
                cpreader.Logger.error(ex);
            }
            SearchInput searchInput = new SearchInput();
            searchInput.fillFromSession(this);

            // access level - add to person model
            int accessLevel = AccessLevelManager.getCurrentAccessLevel();

            if (accessLevel < 1)
            {
                return PartialView("access_denied", new Tuple<int, SearchInput, string>(401, searchInput, "Access denied."));
            }
            if (person == null)
            {
                return PartialView("show_error", new Tuple<int, SearchInput>(503, searchInput));
            }

            if (person.code() == 200)
            {
                return PartialView("modalcontent", new Tuple<IPerson, int>(person, accessLevel));

            }
            else {
                //TODO - A person wasn't found
                return PartialView("show_error", new Tuple<int, SearchInput>(person.code(), searchInput));
            }
        }


        public ActionResult closeDetail()
        {
            cpreader.Logger.info("CLOSE DETAIL ");
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
                    cpreader.Logger.info("OFFLINE SEARCH");
                    this.setOnline(false); // local
                }
                else if (cpreader.Properties.Settings.Default.search_type == 2)
                {
                    cpreader.Logger.info("ONLINE SEARCH");
                    this.setOnline(true); // online
                }
                else
                {
                    cpreader.Logger.info("USE RADIO BUTTON VALUE: " + online);
                    this.setOnline(value); // use radio button value
                }
            }

            public void fillFromSession(Controller controller)
            {
                fillFromSession(controller.Session.Keys.OfType<string>().ToDictionary<string, string, object>(k => k, k => controller.Session[k]));
            }

            public void fillFromSession(HttpContext context)
            {
                fillFromSession(context.Session.Keys.OfType<string>().ToDictionary<string, string, object>(k => k, k => context.Session[k]));
            }

            public void fillFromSession(Dictionary<string, object> controller)
            {
                setQuery(StringUtils.format("{0}", controller.ContainsKey("query") ? controller["query"] : ""));

                setAddressQuery(StringUtils.format("{0}", controller.ContainsKey("addressQuery") ? controller["addressQuery"] : ""));


                if (cpreader.Properties.Settings.Default.search_type == 1)
                {
                    cpreader.Logger.info("OFFLINE SEARCH");
                    this.setOnline(false); // local            
                }
                else if (cpreader.Properties.Settings.Default.search_type == 2)
                {
                    cpreader.Logger.info("ONLINE SEARCH");
                    this.setOnline(true); // online
                }
                else
                {
                    String onlineS = controller.ContainsKey("online") ? controller["online"] as string : "";
                    if ("true".Equals(onlineS))
                        setOnline(true);
                }
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

        public class list_viewModel : Tuple<List<util.cprbroker.IPerson>, int, int, string, SearchInput, int>
        {
            public list_viewModel(List<util.cprbroker.IPerson> i1, int i2, int i3, string i4, SearchInput i5, int i6) : base(i1, i2, i3, i4, i5, i6)
            { }
        }

    }
}