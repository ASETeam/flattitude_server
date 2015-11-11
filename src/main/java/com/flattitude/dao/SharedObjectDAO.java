package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.flattitude.dto.SharedObject;
import com.mysql.jdbc.Statement;

public class SharedObjectDAO {
	
	public int addSharedObject (SharedObject object) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "INSERT INTO SHAREDOBJECT (FLATID, OBJECTNAME, OBJECTDESCRIPTION, LONGITUDE, LATITUDE, EDITIONDATE) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, object.getFlatID());
			ps.setString(2, object.getName());
			ps.setString(3, object.getDescription());
			ps.setFloat(4, object.getLongitude());
			ps.setFloat(5, object.getLatitude());
			ps.setDate(6, new Date(System.currentTimeMillis()));
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			int idObject = rs.getInt(1); 
			
			return idObject;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public List<SharedObject> getNotNotifiedObjects(int flatID, String timestamp) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM SHAREDOBJECT WHERE FLAT_ID = ? AND DATE > ? ";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, flatID);
			
			DateFormat df = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
			java.util.Date result =  df.parse(timestamp);
			
			ps.setDate(2, new Date(result.getTime()));
			
			ResultSet rs = ps.executeQuery();
			
			List<SharedObject> sharedObjects = new ArrayList<SharedObject>();
						
			SharedObject sObject;
			
			while (rs.next()) {
				sObject = new SharedObject();
				sObject.setFlatID(rs.getInt("FLATID"));
				sObject.setName(rs.getString("OBJECTNAME"));
				sObject.setDescription(rs.getString("OBJECTDESCRIPTION"));
				sObject.setLatitude(rs.getFloat("LATITUDE"));
				sObject.setLongitude(rs.getFloat("LONGITUDE"));
				
				sharedObjects.add(sObject);
			}
			
			return sharedObjects;
			
		} catch (Exception ex) {
			throw ex;
		}
		
	}
	
}
