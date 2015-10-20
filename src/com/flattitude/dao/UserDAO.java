package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.flattitude.dto.User;

public class UserDAO {
	
	public boolean login (String email, String password) {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT password FROM Users WHERE email = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String dbPswd = rs.getString("password");
				
				if (dbPswd.equals(password)) return true;
				else return false;
			}
			
			return false;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public boolean register (User user, String password) {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, user.getId());
			ps.setString(2, user.getEmail());
			ps.setString(3, password);
			ps.setString(4, user.getFirstname());
			ps.setString(5, user.getLastname());
			ps.setString(6, user.getPhonenbr());
			ps.setDate(10, new Date(System.currentTimeMillis()));
			
			return ps.execute();
		} catch (Exception ex) {
			return false;
		}
		
	}
}
