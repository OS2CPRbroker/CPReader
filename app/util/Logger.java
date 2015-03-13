package util;

import java.sql.Timestamp;
import java.util.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;


/**
 * Created by Mat Howlett on 03/03/2015.
 */
public class Logger {

	public boolean logError(String message)
	{
		// TODO: log to database
		Timestamp timeStamp = this.getTimeStamp();

        // log to file
        play.Logger.error(message+" logged.");
        return true;
	}

    public boolean logInfo(String message) 
    {   
    	// always log to Play Logger
    	
    	play.Logger.info(message);

        // TODO: log to database
        Timestamp timeStamp = this.getTimeStamp();
        play.Logger.info("Timestamp: "+timeStamp);

        if (logToDb(message, timeStamp))
        {
        	// log to file
        	play.Logger.info("Logged: "+message);
        	return true;
        }
        play.Logger.error("Failed to log: "+message);
        return false;
    }

    private Timestamp getTimeStamp()
    {
    	 java.util.Date date= new java.util.Date();
	 	 return new Timestamp(date.getTime());
    }

    private boolean logToDb(String msg, Timestamp ts)
    {
    	Connection conn = null;
    	
		try 
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				play.Logger.info("Driver created");

				conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cprlogger?" + "user=mat&password=mat");

				Statement m_Statement = conn.createStatement();
			    String query = "SELECT * FROM logs";

			    PreparedStatement preparedStatement = null;
			    String insertTableSQL = "INSERT INTO logs (message) VALUES (?)";
 
				try 
				{
					preparedStatement = conn.prepareStatement(insertTableSQL);
					preparedStatement.setString(1, msg);
					preparedStatement.executeUpdate();
		 
				} 
				catch (SQLException e)
				{
					System.out.println(e.getMessage());
		 
				} 
				finally 
				{
					if (preparedStatement != null) 
					{
						preparedStatement.close();
					}
		 
					if (conn != null) {
						conn.close();
					}
				}
				/*
			    ResultSet m_ResultSet=null;
			    try
			    {
			    	m_ResultSet = m_Statement.executeQuery(query);

			    	//play.Logger.info(m_ResultSet.getString(0));
				    while (m_ResultSet.next()) {
				     	play.Logger.info(m_ResultSet.getString(2));
				    }

			    	play.Logger.info("RESULT EXECUTED");
			    }
			    catch(Exception e)
			   	{
			   		play.Logger.info("FAILED TO EXECUTE " );
			   	}
		    	play.Logger.info("Connection established");		
		    	*/
		    }
		    catch (InstantiationException|ClassNotFoundException|IllegalAccessException e) 
		    {
		    	play.Logger.error("Failed create driver");
		    }
		    conn.close();
		    return true;

		} 
		catch (SQLException ex) 
		{
		    // handle any errors
		    play.Logger.error("SQLException: " + ex.getMessage());
		    play.Logger.error("SQLState: " + ex.getSQLState());
		    play.Logger.error("VendorError: " + ex.getErrorCode());

		}
		return false;
	}
}
