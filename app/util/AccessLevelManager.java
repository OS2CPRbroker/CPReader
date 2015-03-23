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
}
