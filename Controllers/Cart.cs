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
 * Mat Howlett
 *
 * The code is currently governed by OS2 - Offentligt digitaliserings-
 * f?llesskab / http://www.os2web.dk .
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
using System;
using System.Collections.Generic;
using cpreader;
using System.Net;

namespace cpreader.Controllers
{

    /**
     * Created by Mat Howlett on 24/02/2015.
     */

    public class CartController : Controller
    {

        private List<List<String>> cartItems = null; //new ArrayList<List<String>>();
        private static int CART_CACHE_TIMEOUT = 3600; // 2 hours
        private Logger logger = new Logger();

        public ActionResult view()
        {
            // get cart items from cache
            util.Cart cart = util.Cart.fromSession();
            return PartialView("cartviewbody", new Tuple<util.Cart>(cart));
        }

        public static String countTextString()
        {
            return String.Format("{0}", util.Cart.fromSession().Persons.Count);
        }

        public ActionResult countText()
        {
            return Content(countTextString());
        }

        public ActionResult empty()
        {
            cpreader.Logger.info("clearing cart");
            util.Cart cart = util.Cart.fromSession();
            cart.clear();
            cart.saveToSession();
            return new HttpStatusCodeResult(HttpStatusCode.OK);
        }

        public ActionResult addItem(String uuid)
        {
            cpreader.Logger.info("adding " + uuid);
            util.Cart cart = util.Cart.fromSession();
            String ret = cart.add(uuid);
            cart.saveToSession();
            return Content(ret);
        }

        public ActionResult removeItem(String uuid)
        {
            cpreader.Logger.info("removing " + uuid);
            util.Cart cart = util.Cart.fromSession();
            String ret = cart.remove(uuid);
            cart.saveToSession();
            return Content(ret.ToString());
        }
    }

}