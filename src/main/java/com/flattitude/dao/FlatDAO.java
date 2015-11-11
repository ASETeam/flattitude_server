package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.flattitude.dto.Flat;
import com.mysql.jdbc.Statement;

public class FlatDAO {
	
	public int create (Flat flat) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "INSERT INTO FLAT (NAME, COUNTRY, CITY, POSTCODE, ADDRESS, IBAN, CREATIONTIME, DELETIONTIME) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, NULL)";
			
			PreparedStatement ps = con.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, flat.getName());
			ps.setString(2, flat.getCountry());
			ps.setString(3, flat.getCity());
			ps.setString(4, flat.getPostcode());
			ps.setString(5, flat.getAddress());
			ps.setString(6, flat.getIban());
			ps.setDate(7, new Date(System.currentTimeMillis()));
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			int idFlat = rs.getInt(1);
			
			return idFlat;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Flat getInfo(String flatid) throws Exception {
		
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM FLAT WHERE ID = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, Integer.valueOf(flatid));
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			Flat flat = new Flat(
					rs.getString("NAME"),
					rs.getString("COUNTRY"),
					rs.getString("CITY"),
					rs.getString("POSTCODE"),
					rs.getString("ADDRESS"),
					rs.getString("IBAN"));
			
			return flat;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
}
