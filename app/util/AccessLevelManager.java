package util;

import play.cache.Cache;
import play.mvc.Http.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Beemen on 23/03/2015.
 */
public abstract class AccessLevelManager {
    public static int getCurrentAccessLevel(){
        Integer accessLevel=0;
        if (Cache.get("accesslevel") != null)
        {
            accessLevel = Integer.parseInt(Cache.get("accesslevel").toString());
        }
        return accessLevel;
    }

    public static void setCurrentAccessLevel(String accessLevel) {
        Cache.set("accesslevel", accessLevel, 3600); // good for 2 hours
    }

    public static void initGroups()
    {
        String groupslist = "group0, anothergroup, andanothergroup";
        Context.current().session().put("grouplist", groupslist);
    }

    public static String getAccessLevel(String username)
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
            String grouplist = Context.current().session().get("grouplist");
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
                                    Context.current().session().put("usecart", "" + tokens[2]);
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
                            Context.current().session().put("usecart", "" + tokens[2]);
                        }
                    }
                }
            }
            // override any cart access if the master cart usage is set to false
            if (play.Play.application().configuration().getString("cart.enabled").equals("false"))
            {
                // disable for all
                Context.current().session().put("usecart", "0");
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
}
