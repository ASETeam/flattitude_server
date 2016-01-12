package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.flattitude.dto.User;
import com.mysql.jdbc.Statement;

public class UserDAO {
	
	public int login (String email, String password) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT password FROM USER WHERE email = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String dbPswd = rs.getString("password");
				
				if (dbPswd.equals(password)) return getID(email);
				else return -1;
			}
			
			return -1;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public int register (User user, String password) throws Exception{
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "INSERT INTO USER (EMAIL, PASSWORD, FIRSTNAME, LASTNAME, PHONENBR, BIRTHDAY, IBAN, PICTURE, CREATIONTIME)"
					+ " VALUES (?, ?, ?, ?, ?, NULL, NULL, NULL, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, user.getEmail());
			ps.setString(2, password);
			ps.setString(3, user.getFirstname());
			ps.setString(4, user.getLastname());
			ps.setString(5, user.getPhonenbr());
			
			java.util.Date dt = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(dt.getTime());
			
			ps.setTimestamp(6, timestamp);
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			
			return rs.getInt(1);
		} catch (SQLException ex) {
			throw ex;
		}		
	}

	public User getInfoUser (int idUser) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM USER WHERE ID = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, idUser);
			
			ResultSet rs = ps.executeQuery();
			
			User user = null;
			
			while (rs.next()) {
				String email = rs.getString("EMAIL");
				String firstName = rs.getString("FIRSTNAME");
				String lastName = rs.getString("LASTNAME");
				String phoneNbr = rs.getString("PHONENBR");
				
				Date birthDate = rs.getDate("BIRTHDAY");
				
				String IBAN = rs.getString("IBAN");
				
				user = new User(email, firstName, lastName);
				user.setPhonenbr(phoneNbr);
				user.setBirthdate(birthDate);
				user.setIban(IBAN);
			}
			
			return user;
		} catch (Exception ex) {
			throw ex;
		}
		
	}
	
	public User getInfoUser (String email) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM USER WHERE EMAIL = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
			
			User user = null;
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String firstName = rs.getString("FIRSTNAME");
				String lastName = rs.getString("LASTNAME");
				String phoneNbr = rs.getString("PHONENBR");
				
				Date birthDate = rs.getDate("BIRTHDAY");
				
				String IBAN = rs.getString("IBAN");
				
				user = new User(email, firstName, lastName);
				user.setPhonenbr(phoneNbr);
				user.setBirthdate(birthDate);
				user.setIban(IBAN);
				user.setId(id);
			}
			
			return user;
		} catch (Exception ex) {
			throw ex;
		}
		
	}	
	
	public String updateToken (String token, int id) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT TOKEN FROM USER WHERE id = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			String oldToken = rs.getString("TOKEN");
			
			if (oldToken == null) {
				stmt = "UPDATE USER SET token = ? WHERE id = ?";
				
				ps = con.prepareStatement(stmt);
				ps.setString(1, token);
				ps.setInt(2, id);
				
				ps.executeUpdate();
				
				return token;
				
			} else return oldToken;
			
			
		} catch (Exception ex) {
			throw ex;
		}
	} 
	
	public void deleteToken (String id) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "UPDATE USER SET token = NULL WHERE id = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setString(1, id);
			
			ps.executeUpdate();
			
		} catch (Exception ex) {
			throw ex;
		}
	} 
	
	public boolean checkToken (String token) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT TOP 1 ID FROM USER WHERE TOKEN = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setString(1, token);
			
			ResultSet rs = ps.executeQuery();
			boolean exists = rs.next();
			
			return exists;
			
		} catch (Exception ex) {
			throw ex;
		}
		
	}
	
	private int getID (String email) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT id FROM USER WHERE email = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			return rs.getInt(1);
		} catch (SQLException ex) {
			throw ex;
		}
	}
	
	
}
