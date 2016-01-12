package com.flattitude.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
			
			ps.setTimestamp(7, timestamp);
			
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
			
			ResultSet rs = ps.executeQuery();
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

	public float getFlatBalance(int flatid) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM FLAT WHERE ID = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, Integer.valueOf(flatid));
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			return Float.valueOf(rs.getFloat("BALANCE"));
			
		} catch (Exception ex) {
			throw ex;
		}
	}

	public boolean modifyBalance(int flatid, float amount) throws Exception{
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "UPDATE FLAT SET BALANCE = ? WHERE ID = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setFloat(1, amount);
			ps.setInt(2, flatid);
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
}
