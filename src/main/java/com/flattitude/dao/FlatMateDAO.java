package com.flattitude.dao;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import com.flattitude.dto.Flat;

public class FlatMateDAO {
	
	public Set<Flat> getInvitations(int idUser) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT FLAT_ID, FLAT.NAME, FLAT.COUNTRY, FLAT.CITY, FLAT.POSTCODE, FLAT.ADDRESS, FLAT.IBAN FROM USER_FLAT " 
						+ "INNER JOIN FLAT ON FLAT.ID = FLAT_ID "
						+ "WHERE USER_ID = ? AND JOINEDTIME IS NULL ";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, idUser);
			ResultSet rs = ps.executeQuery();
			
			Set<Flat> infoFlat = new HashSet<Flat>();
			
			while (rs.next()) {
				Flat flat = new Flat(
						rs.getString("FLAT.NAME"),
						rs.getString("FLAT.COUNTRY"),
						rs.getString("FLAT.CITY"),
						rs.getString("FLAT.POSTCODE"),
						rs.getString("FLAT.ADDRESS"),
						rs.getString("FLAT.IBAN"));
				
				flat.setID(rs.getInt("FLAT_ID"));
				
				infoFlat.add(flat);
			}
			
			return infoFlat;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
	
	public Flat getUserFlat (int idUser) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT FLAT_ID, FLAT.NAME, FLAT.COUNTRY, FLAT.CITY, FLAT.POSTCODE, FLAT.ADDRESS, FLAT.IBAN FROM USER_FLAT " 
						+ "INNER JOIN FLAT ON FLAT.ID = USER_FLAT.FLAT_ID "
						+ "WHERE USER_FLAT.USER_ID = ? AND USER_FLAT.ISACTIVE = TRUE ";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, idUser);
			ResultSet rs = ps.executeQuery();
			
			Flat infoFlat = null;
			
			while (rs.next()) {				
				infoFlat = new Flat(
						rs.getString("FLAT.NAME"),
						rs.getString("FLAT.COUNTRY"),
						rs.getString("FLAT.CITY"),
						rs.getString("FLAT.POSTCODE"),
						rs.getString("FLAT.ADDRESS"),
						rs.getString("FLAT.IBAN"));
				
				infoFlat.setID(rs.getInt("FLAT_ID"));
			}
			
			return infoFlat;
		} catch (Exception sqlex) {
			throw sqlex;
		}
		
	}
	
	public boolean createInvitation(int idUser, int idFlat) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "INSERT INTO USER_FLAT (USER_ID, FLAT_ID, ISMASTER, INVITEDTIME, JOINEDTIME, LEFTTIME, ISACTIVE) "
					+ "VALUES (?, ?, TRUE, ?, NULL, NULL, FALSE)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setInt(1, idUser);
			ps.setInt(2, idFlat);
			
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
			
			ps.setTimestamp(3, timestamp);
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
	
	public boolean acceptInvitation(int idUser, int idFlat) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "UPDATE USER_FLAT SET JOINEDTIME = ?, ISACTIVE = TRUE WHERE USER_ID = ? AND FLAT_ID = ? AND ISACTIVE = FALSE";
			PreparedStatement ps = con.prepareStatement(stmt);
			
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
			
			ps.setTimestamp(1, timestamp);
			
			ps.setInt(2, idUser);
			ps.setInt(3, idFlat);
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}

	}
	
	public boolean deleteInvitation(int idUser, int idFlat) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "DELETE FROM USER_FLAT WHERE USER_ID = ? AND FLAT_ID = ?";
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setInt(1, idUser);
			ps.setInt(2, idFlat);

			ps.executeUpdate();
			
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}

	public boolean assignFlat(int idUser, int idFlat, boolean isMaster) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "INSERT INTO USER_FLAT (USER_ID, FLAT_ID, ISMASTER, INVITEDTIME, JOINEDTIME, LEFTTIME, ISACTIVE) "
					+ "VALUES (?, ?, ?, ?, ?, NULL, TRUE)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setInt(1, idUser);
			ps.setInt(2, idFlat);
			ps.setBoolean(3, isMaster);
			
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
			
			ps.setTimestamp(4, timestamp);
			ps.setTimestamp(5, timestamp);
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}
		
	}
	
	public boolean quitFlat(int idUser, int idFlat) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "UPDATE USER_FLAT SET ISACTIVE = FALSE, LEFTTIME = ? WHERE USER_ID = ? AND FLAT_ID = ? AND ISACTIVE = TRUE";
					
			PreparedStatement ps = con.prepareStatement(stmt);
			
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
			ps.setTimestamp(1, timestamp);
			
			ps.setInt(2, idUser);
			ps.setInt(3, idFlat);
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}
		
	}

	public List<Integer> getFlatMembers(Integer flatId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT USER_ID FROM USER_FLAT WHERE ISACTIVE = TRUE AND FLAT_ID = ?";
					
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, flatId);
						
			ResultSet rs = ps.executeQuery();
			
			List<Integer> results = new ArrayList<Integer>();
			
			while (rs.next()) {
				results.add(rs.getInt(1));
			}
			
			return results;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}

	public float getUserBalance(int userid) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT BALANCE FROM USER_FLAT WHERE USER_ID = ? AND ISACTIVE = 1";
					
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, userid);
						
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			float balance = rs.getFloat("BALANCE");
			
			return balance;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}

	public boolean modifyBalance(int userid, float amount) throws Exception  {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "UPDATE USER_FLAT SET BALANCE = ? WHERE USER_ID = ? AND ISACTIVE = 1";
					
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setFloat(1, amount);
			ps.setInt(2, userid);
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}	
	
}
