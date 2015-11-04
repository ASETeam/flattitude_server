package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class FlatMateDAO {
	
	//Make join??
	public Map<String, String> getInvitations(int idUser) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT FLAT_ID, FLAT.NAME FROM USER_FLAT " 
						+ "INNER JOIN FLAT ON FLAT.ID = FLAT_ID "
						+ "WHERE USER_ID = ? AND JOINEDTIME IS NULL ";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, idUser);
			ResultSet rs = ps.executeQuery();
			
			Map<String, String> infoFlat = new HashMap<String, String>();
			
			while (rs.next()) {
				infoFlat.put(rs.getString(1), rs.getString(2));
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
			ps.setDate(3, new Date(System.currentTimeMillis()));
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
	
	public boolean acceptInvitation(int idUser, int idFlat) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "UPDATE FROM USER_FLAT SET JOINEDTIME = ? WHERE USER_ID = ? AND FLAT_ID = ? ";
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setDate(1, new Date(System.currentTimeMillis()));
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
			ps.setDate(4, new Date(System.currentTimeMillis()));
			ps.setDate(5, new Date(System.currentTimeMillis()));
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}
		
	}
	
	
	/*public boolean quit() {
		
	}*/
}
