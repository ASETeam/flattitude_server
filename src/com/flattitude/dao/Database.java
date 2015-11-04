package com.flattitude.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	
	public Connection Get_Connection() throws Exception
	{
		try
		{
			String connectionURL = "jdbc:mysql://127.11.227.130:3306/flattiserver";
			Connection connection = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "adminSpKe4EG", "5lNar9GztR3u");
		    return connection;
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		}
	}

}
