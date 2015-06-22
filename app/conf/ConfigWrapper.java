package conf;

import play.Configuration;

/**
 * Created by Beemen on 22/06/2015.
 */
public class ConfigWrapper {
    private static Configuration getConfiguration(){
        return play.Play.application().configuration();
    }

    public static Boolean cartEnabled() {
        return  getConfiguration().getBoolean("cart.enabled",true);
    }

    public static Boolean cartAllowNonBornPersons() {
        return getConfiguration().getBoolean("cart.allownonbornpersons",true);
    }
}
