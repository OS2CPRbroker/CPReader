using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace cpreader
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

            //# Routes
            //# This file defines all application routes (Higher priority routes first)
            //# ~~~~

            //# Home page
            //            GET / @controllers.Home.index()
            routes.MapRoute("index", "", new { controller = "Home", action = "index" });

            //# Enable webjar based resources to be returned - used in development
            //#GET 	/webjars/*file  			controllers.WebJarAssets.at(file)

            //# Login
            //GET / login                                                       controllers.Signon.login()
            //POST / login                                                       @controllers.Signon.authenticate()
            //GET / logout                                                      controllers.Signon.logout()

            //# Search
            //POST / search / cpr / @controllers.Search.getUuidFromCpr()
            routes.MapRoute("search1", "search/cpr/", new { controller = "Search", action = "getUuidFromCpr" });
            //GET / search / update /:uuid / @controllers.Search.updatePerson(uuid: String)
            routes.MapRoute("search2", "search/update/{uuid}", new { controller = "Search", action = "updatePerson" });
            //GET / search / updateparents /:uuid / @controllers.Search.updateParents(uuid: String)
            routes.MapRoute("search3", "search/updateparents/{uuid}", new { controller = "Search", action = "updateParents" });
            //GET / search / updatefullpage /:uuid / @controllers.Search.updateFullpage(uuid: String)
            routes.MapRoute("search4", "search/updatefullpage/{uuid}", new { controller = "Search", action = "updateFullpage" });
            //POST / search / closedetail                                           @controllers.Search.closeDetail()
            routes.MapRoute("search5", "search/closedetail", new { controller = "Search", action = "closeDetail" });

            //GET / search / name /:name / address /:address / page /:page @controllers.Search.searchNameAndAddress(name: String, address: String, online: Boolean ?= false, page: Int)
            routes.MapRoute("search6", "search/name/{name}/address/{address}/page/{page}", new { controller = "Search", action = "searchNameAndAddress", online = false });
            //GET / search / name /:name / address /:address / online / page /:page @controllers.Search.searchNameAndAddress(name: String, address: String, online: Boolean ?= true, page: Int)
            routes.MapRoute("search7", "search/name/{name}/address/{address}/online/page/{page}", new { controller = "Search", action = "searchNameAndAddress", online = true });

            //GET / search / name /:name / page /:page @controllers.Search.searchNameAndAddress(name: String, address = "", online: Boolean ?= false, page: Int)
            routes.MapRoute("search8", "search/name/{name}/page/{page}", new { controller = "Search", action = "searchNameAndAddress", address = "", online = false });
            //GET / search / name /:name / online / page /:page @controllers.Search.searchNameAndAddress(name: String, address = "", online: Boolean ?= true, page: Int)
            routes.MapRoute("search9", "search/name/{name}/online/page/{page}", new { controller = "Search", action = "searchNameAndAddress", address = "", online = true });
            //GET / search / address /:address / page /:page @controllers.Search.searchNameAndAddress(name = "", address: String, online: Boolean ?= false, page: Int)
            routes.MapRoute("search10", "search/address/{address}/page/{page}", new { controller = "Search", action = "searchNameAndAddress", name="", online=false });
            //GET / search / address /:address / online / page /:page @controllers.Search.searchNameAndAddress(name = "", address: String, online: Boolean ?= true, page: Int)
            routes.MapRoute("search11", "search/address/{address}/online/page/{page}", new { controller = "Search", action = "searchNameAndAddress", name="", online=true });

            //GET / show / uuid /:uuid / @controllers.Search.showPerson(uuid: String)
            //#GET         /showfull/uuid/:uuid/                                        @controllers.Search.showPersonFull(uuid : String)
            routes.MapRoute("search12", "show/uuid/{uuid}/", new { controller = "Search", action = "showPerson" });

            //# Map static resources from the /public folder to the /assets URL path
            //GET / assets/*file                                                controllers.Assets.at(path="/public", file)

            //# Shopping cart
            //GET         /cart/add/:uuid/                                             @controllers.Cart.addItem(uuid : String)
            routes.MapRoute("cart1", "cart/add/{uuid}", new { controller = "Cart", action = "addItem" });
            //GET         /cart/remove/:uuid/                                          @controllers.Cart.removeItem(uuid : String)
            routes.MapRoute("cart2", "cart/remove/{uuid}", new { controller = "Cart", action = "removeItem" });
            //GET         /cart/view/                                                  @controllers.Cart.view()
            routes.MapRoute("cart3", "cart/view/", new { controller = "Cart", action = "view" });
            //GET         /cart/count/                                                 controllers.Cart.countText()
            routes.MapRoute("cart4", "cart/count/", new { controller = "Cart", action = "countText" });
            //GET         /cart/empty/                                                 @controllers.Cart.empty()
            routes.MapRoute("cart5", "cart/empty", new { controller = "Cart", action = "empty" });


            //routes.MapRoute(
            //    name: "Default",
            //    url: "{controller}/{action}/{id}",
            //    defaults: new { controller = "Home", action = "Index", id = UrlParameter.Optional }
            //);

            // Error page
            routes.MapRoute("showSearchError", "search/error/{httpErrorCode}/", new { controller = "Search", action = "showError"});
        }
    }
}
