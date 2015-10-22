package com.flattitude.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
			
			int idFlat = ps.executeUpdate();
			
			return idFlat;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
}
