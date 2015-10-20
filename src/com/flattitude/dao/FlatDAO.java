package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.flattitude.dto.Flat;
import com.flattitude.dto.User;

public class FlatDAO {
	
	public boolean create (Flat flat) {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "INSERT INTO FLAT VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, flat.getId());
			ps.setString(2, flat.getName());
			ps.setString(3, flat.getCountry());
			ps.setString(4, flat.getCity());
			ps.setString(5, flat.getPostcode());
			ps.setString(6, flat.getAddress());
			ps.setString(7, flat.getIban());
			ps.setDate(8, new Date(System.currentTimeMillis()));
			
			return ps.execute();
		} catch (Exception ex) {
			return false;
		}
	}
	
}
