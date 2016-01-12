package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.flattitude.dto.Flat;
import com.flattitude.dto.SharedObject;
import com.mysql.jdbc.Statement;

public class SharedObjectDAO {
	
	public int addSharedObject (SharedObject object) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "INSERT INTO OBJECT (flat_id, name, description) "
					+ "VALUES (?, ?, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, object.getFlatID());
			ps.setString(2, object.getName());
			ps.setString(3, object.getDescription());
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			int idObject = rs.getInt(1);
			
			stmt = "INSERT INTO LOCALIZATION (user_id, object_id, lng, lat, time) "
					+ "VALUES (?, ?, ?, ?, ?)";
			
			ps = con.prepareStatement(stmt);
					
			ps.setInt(1, object.getUserID());
			ps.setInt(2, idObject);
			ps.setFloat(3, object.getLongitude());
			ps.setFloat(4, object.getLatitude());
			
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
			
			ps.setTimestamp(5, timestamp);
			
			ps.executeUpdate();
			
			return idObject;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public boolean editSharedObject (SharedObject object) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			if (object.getName() != null || object.getDescription() != null) {
				String stmt = "UPDATE OBJECT SET ";
				
				if (object.getName() != null) stmt += " NAME = ? "; 
				if (object.getDescription() != null) stmt += " , DESCRIPTION = ?";
				
				stmt += " WHERE OBJECTID = ? ";
				
				PreparedStatement ps = con.prepareStatement(stmt);
				int countPars = 1;
				
				if (object.getName() != null) { ps.setString(countPars, object.getName()); countPars++;}
				if (object.getDescription() != null) { ps.setString(countPars, object.getDescription()); countPars++;}
	
				ps.setInt(countPars, object.getID());
				
				ps.executeUpdate();
			}
			
			if (object.getLongitude() != 0.0f || object.getLatitude() != 0.0f) {
				String stmt = "UPDATE LOCALIZATION SET ";
				
				if (object.getLongitude() != 0.0f) stmt += " LNG = ? "; 
				if (object.getLatitude() != 0.0f) stmt += " , LAT = ?";
				
				stmt += " WHERE OBJECTID = ? AND TIME = ?";
				
				PreparedStatement ps = con.prepareStatement(stmt);
				int countPars = 1;
				
				if (object.getLongitude() != -1) { ps.setFloat(countPars, object.getLongitude()); countPars++;}
				if (object.getLatitude() != -1) { ps.setFloat(countPars, object.getLatitude()); countPars++;}
	
				ps.setInt(countPars, object.getID());
				
				java.util.Date today = new java.util.Date();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
				
				ps.setTimestamp(countPars+1, timestamp);
				
				ps.executeUpdate();
			}
			
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public boolean quitSharedObject (int idObject) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "DELETE FROM OBJECT WHERE ID = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, idObject);
			
			ps.executeUpdate();
			
			stmt = "DELETE FROM LOCALIZATION WHERE OBJECT_ID = ?";
			
			ps = con.prepareStatement(stmt);
			ps.setInt(1, idObject);
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
		
	public Set<SharedObject> getUserNotNotifiedObjects(int idUser) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT * FROM OBJECT "
					+ "INNER JOIN LOCALIZATION ON OBJECT.ID = LOCALIZATION.OBJECT_ID "
					+ "INNER JOIN OBJECT_POSITION_NOTIFICATION ON OBJECT_POSITION_NOTIFICATION.OBJECT_ID = ID "
					+ "INNER JOIN USER_NOTIFICATION ON OBJECT_POSITION_NOTIFICATION.ID = USER_NOTIFICATION.NOTIFICATION_ID "
					+ "WHERE USER_NOTIFICATION.USER_ID = ? AND USER_NOTIFICATION.SEEN_DATE IS NULL";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, idUser);
			ResultSet rs = ps.executeQuery();
			
			Set<SharedObject> sharedObjects = new HashSet<SharedObject>();
			
			SharedObject sObject;
			
			while (rs.next()) {
				sObject = new SharedObject();
				sObject.setFlatID(rs.getInt("FLAT_ID"));
				sObject.setName(rs.getString("NAME"));
				sObject.setDescription(rs.getString("DESCRIPTION"));
				sObject.setLatitude(rs.getFloat("LAT"));
				sObject.setLongitude(rs.getFloat("LNG"));
				
				sharedObjects.add(sObject);
			}
			
			return sharedObjects;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}

	public List<SharedObject> getFlatObjects(Integer flatId) throws Exception {
		
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM OBJECT "
					+ "INNER JOIN LOCALIZATION ON LOCALIZATION.OBJECT_ID = OBJECT.ID "
					+ "WHERE FLAT_ID = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, flatId);

			ResultSet rs = ps.executeQuery();
			
			List<SharedObject> sharedObjects = new ArrayList<SharedObject>();
						
			SharedObject sObject;
			
			while (rs.next()) {
				sObject = new SharedObject();
				sObject.setID(rs.getInt("ID"));
				sObject.setFlatID(rs.getInt("FLAT_ID"));
				sObject.setName(rs.getString("NAME"));
				sObject.setDescription(rs.getString("DESCRIPTION"));
				sObject.setLatitude(rs.getFloat("LAT"));
				sObject.setLongitude(rs.getFloat("LNG"));
				sObject.setTime(rs.getDate("TIME"));
				sObject.setUserID(rs.getInt("USER_ID"));
				sharedObjects.add(sObject);
			}
			
			return sharedObjects;
			
		} catch (Exception ex) {
			throw ex;
		}
	}
	
}
