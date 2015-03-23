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

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;

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
    
    /*public Result index() 
    {
        // get cart items from cache
        List<List<String>> cartItems = (List<List<String>>) Cache.get("cartdata");

        if (cartItems == null)
        {
            cartItems = new ArrayList<List<String>>();
        }
        return ok(views.html.viewcart.render(cartItems.size(), cartItems));
    }*/

    public Result view() 
    {

        //logger.logInfo("test");
        // get cart items from cache
        List<List<String>> cartItems = (List<List<String>>) Cache.get("cartdata");
        if (cartItems == null)
        {
            cartItems = new ArrayList<List<String>>();
        }
    	return ok(views.html.viewcart.render(cartItems.size(), cartItems));
    }

    public Result empty(String uri) 
    {
    	cartItems = new ArrayList<List<String>>();
        Cache.set("cartdata", cartItems, CART_CACHE_TIMEOUT);
        flash("message", "Cart emptied.");
        Cache.set("numcartitems", cartItems.size(), CART_CACHE_TIMEOUT);
        // redirect to where we came from
        String[] segments = uri.split("/");
        return redirect(uri);
    }

    public Result copy(String uri) throws UnsupportedFlavorException, IOException 
    {
    	Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	StringSelection copiedData = new StringSelection("");
    	StringBuilder stringBuilder = new StringBuilder();
        // get cart items from cache
        List<List<String>> cartItems = (List<List<String>>) Cache.get("cartdata");
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
            flash("message", itemscopied + " items copied to the clipboard.");
            //return ok(views.html.viewcart.render(cartItems.size(), cartItems));
            return redirect(uri);
        }
        else
        {
            return ok("Nothing to copy");
        }
    	
    }


    //public Result addItem(String firstname, String lastname, String uri)
    public Result addItem(String cprnum, String uri)
    {

        play.Logger.info("adding " + cprnum);
        boolean exists=false;
        List<String> personData = new ArrayList<String>();
        
        
        if (session(cprnum+"_cprnum") != null)
        {

            personData.add(session(cprnum+"_fname"));
            personData.add(session(cprnum+"_mname"));
            personData.add(session(cprnum+"_lname"));
            personData.add(session(cprnum+"_cprnum"));

            //logger.logInfo("ID FROM SESSION: " + session(firstname+lastname));

            // get cart items from cache
            List<List<String>> cartItems = (List<List<String>>) Cache.get("cartdata");

            if (cartItems == null)
            {
                cartItems = new ArrayList<List<String>>();
            }

            // scan through and see if the person already exists
            for (int i = 0;  i < cartItems.size(); i++)
            {
                String searchByCprNum = cartItems.get(i).get(3);
                if(searchByCprNum.equals(cprnum))
                {
                    exists=true;
                }
            }
            if(!exists) 
            {
                
                cartItems.add( personData );
                flash("message", session(cprnum+"_fname") + " " + session(cprnum+"_mname") + " " + session(cprnum+"_lname") + "has been added to the cart.");
            }
            else
            {
                flash("message", session(cprnum+"_fname") + " " + session(cprnum+"_mname") + " " + session(cprnum+"_lname") + "has aready been added to the cart.");
            }
            Cache.set("numcartitems", cartItems.size(), CART_CACHE_TIMEOUT);
            Cache.set("cartdata", cartItems, CART_CACHE_TIMEOUT);
        } 
        else
        {
            play.Logger.info("problem adding " + cprnum);
            //logger.logInfo("NO SUCH PERSON: " + session(firstname+lastname));
        }
        return redirect(uri);
    }

    public Result removeItem(String cprnum, String uri)
    {
        // get cart items from cache
        List<List<String>> cartItems = (List<List<String>>) Cache.get("cartdata");

        if (cartItems == null)
        {
            cartItems = new ArrayList<List<String>>();
        }

     	for ( int i = 0;  i < cartItems.size(); i++)
     	{
     		
            String searchByCprNum = cartItems.get(i).get(3);
            if(searchByCprNum.equals(session(cprnum+"_cprnum")))
            {
                cartItems.remove(i);
                flash("message", session(cprnum+"_fname") + " " + session(cprnum+"_mname") + " "+ session(cprnum+"_lname") + " removed from the cart.");
            }
        }
        Cache.set("numcartitems", cartItems.size(), CART_CACHE_TIMEOUT);
        Cache.set("cartdata", cartItems, CART_CACHE_TIMEOUT);
        return redirect(uri);
    }
 }

