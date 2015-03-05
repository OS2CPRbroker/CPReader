package util;

/**
 * Created by Beemen on 14/11/2014.
 */
public class Logger {

    public boolean logMessage(String message) 
    {   
        // TODO: log to database

        // log to file
        play.Logger.info(message+" logged.");
        return true;
    }
}
