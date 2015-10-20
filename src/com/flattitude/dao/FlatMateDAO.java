package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class FlatMateDAO {
	
	//Make join??
	public Map<String, String> getInvitations(int idUser)  {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT FLAT_ID, FLAT.NAME FROM USER_FLAT "
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
			return null;
		}
	}
	
	public boolean createInvitation(int idUser, int idFlat) {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "INSERT INTO USER_FLAT (USER_ID, FLAT_ID, ISMASTER, INVITEDTIME, JOINEDTIME, LEFTTIME, ISACTIVE) "
					+ "VALUES (?, ?, TRUE, ?, NULL, NULL, FALSE)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setInt(1, idUser);
			ps.setInt(2, idFlat);
			ps.setDate(3, new Date(System.currentTimeMillis()));
			
			ps.executeQuery();
			
			return true;
		} catch (Exception sqlex) {
			return false;
		}
	}
	
	public boolean acceptInvitation(int idUser, int idFlat) {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "UPDATE FROM USER_FLAT WHERE USER_ID = ? AND FLAT_ID = ? SET JOINEDTIME = ?";
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setInt(1, idUser);
			ps.setInt(2, idFlat);
			ps.setDate(3, new Date(System.currentTimeMillis()));

			return ps.execute();
			
		} catch (Exception sqlex) {
			return false;
		}

	}
	
	public boolean deleteInvitation(int idUser, int idFlat) {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "DELETE FROM USER_FLAT WHERE USER_ID = ? AND FLAT_ID = ?";
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setInt(1, idUser);
			ps.setInt(2, idFlat);

			return ps.execute();
			
		} catch (Exception sqlex) {
			return false;
		}
	}

	public boolean assignFlat(int idUser, int idFlat, boolean isMaster) {
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
			
			ps.executeQuery();
			
			return true;
		} catch (Exception sqlex) {
			return false;
		}
		
	}
	
	
	/*public boolean quit() {
		
	}*/
}
