package com.flattitude.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	
	public Connection Get_Connection() throws Exception
	{
		try
		{
		//mysql -h flattitude-db.c8on6uurpxpe.us-west-2.rds.amazonaws.com -P 3306 -u root -p
			String connectionURL = "jdbc:mysql://flattitude-db.c8on6uurpxpe.us-west-2.rds.amazonaws.com:3306/flattitude_db";
			Connection connection = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "root", "flattitude");
		    return connection;
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		}
	}

}
