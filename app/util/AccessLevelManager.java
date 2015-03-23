package util;

import play.cache.Cache;

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
}
