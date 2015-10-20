package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.flattitude.dto.User;

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
			String stmt = "INSERT INTO USER VALUES (?, ?, ?, ?, ?, ?, NULL, NULL, NULL, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, user.getId());
			ps.setString(2, user.getEmail());
			ps.setString(3, password);
			ps.setString(4, user.getFirstname());
			ps.setString(5, user.getLastname());
			ps.setString(6, user.getPhonenbr());
			ps.setDate(7, new Date(System.currentTimeMillis()));
			
			ps.executeUpdate();
			
			return getID(user.getEmail());
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
				String birthDate = rs.getString("BIRTHDATE");
				String IBAN = rs.getString("IBAN");
				
				user = new User(email, firstName, lastName);
				user.setPhonenbr(phoneNbr);
				user.setBirthdate(Date.valueOf(birthDate));
				user.setIban(IBAN);
			}
			
			return user;
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
