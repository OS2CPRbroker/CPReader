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

package controllers;

import play.data.Form;
import play.mvc.Controller;
import play.cache.Cache;
import play.mvc.Result;
import play.mvc.Security;
import util.auth.Secured;
import views.html.index;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import util.cprbroker.IPerson;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import play.i18n.Messages;

import util.Logger;

/**
 * Created by Mat Howlett on 24/02/2015.
 */
@Singleton
public class Cart extends Controller 
{
	private List<List<String>> cartItems = null; //new ArrayList<List<String>>();
	private static final int CART_CACHE_TIMEOUT = 3600; // 2 hours
    private Logger logger = new Logger();

    @Security.Authenticated(Secured.class)
    public Result view()
    {
        // get cart items from cache
        util.Cart cart = util.Cart.fromSession();
        return ok(views.html.cartviewbody.render(cart));
    }

    public Result empty()
    {
    	play.Logger.info("clearing cart");
        util.Cart cart = util.Cart.fromSession();
        cart.clear();
        cart.saveToSession();
        return ok();
    }

    public Result copy(String uri) throws UnsupportedFlavorException, IOException 
    {
    	Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	StringSelection copiedData = new StringSelection("");
    	StringBuilder stringBuilder = new StringBuilder();
        
        // get cart items from cache
        cartItems = (List<List<String>>) Cache.get("cartdata");
        int itemscopied = 0;
        if (cartItems != null)
        {
        	for ( int i = 0;  i < cartItems.size(); i++)
         	{
         		// copy one number per line
         		stringBuilder.append(cartItems.get(i).get(3)+"\n");
                itemscopied++;
         	}

         	String finalString = stringBuilder.toString();

    		copiedData = new StringSelection(finalString);	
            clipBoard.setContents(copiedData, copiedData);

            Transferable t = clipBoard.getContents( null );

            if ( t.isDataFlavorSupported(DataFlavor.stringFlavor) )
            {
                Object o = t.getTransferData( DataFlavor.stringFlavor );
                String data = (String)t.getTransferData( DataFlavor.stringFlavor );
            }
            flash("message", itemscopied + " " + Messages.get("cart.copied"));


            //return ok(views.html.viewcart.render(cartItems.size(), cartItems));
            return redirect(uri);
        }
        else
        {
            return ok("Nothing to copy");
        }
    }

    public Result addItem(String uuid)
    {
        play.Logger.info("adding " + uuid);
        util.Cart cart = util.Cart.fromSession();
        String ret = cart.add(uuid);
        cart.saveToSession();
        return ok(ret);
    }

    public Result removeItem(String uuid)
    {
        play.Logger.info("removing " + uuid);
        util.Cart cart = util.Cart.fromSession();
        String ret = cart.remove(uuid);
        cart.saveToSession();
        return ok(ret.toString());
    }
}

