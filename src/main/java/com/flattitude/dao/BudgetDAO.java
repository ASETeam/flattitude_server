package com.flattitude.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.flattitude.dto.BudgetOperation;
import com.flattitude.dto.Task;
import com.mysql.jdbc.Statement;

public class BudgetDAO {
	
	public List<BudgetOperation> getFlatHistoric (int flatid) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM BUDGET_OPERATION WHERE FLAT = ? ORDER BY DATE DESC";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, flatid);
			
			ResultSet rs = ps.executeQuery();
			
			List<BudgetOperation> operations = new ArrayList<BudgetOperation>();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int idflat = rs.getInt(2);
			    int iduser = rs.getInt(3);
				
				if (rs.wasNull()) {
			        iduser = -1;
			    }
				
				float amount = rs.getFloat(4);
				Date date = rs.getDate(5);
				String description = rs.getString(6);
				
			    
				BudgetOperation bo = new BudgetOperation (
							id,
							idflat,
							iduser,
							amount,
							date,
							description
						);
				
				operations.add(bo);
			}
			
			return operations;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public boolean createBudgetOperation (BudgetOperation bo) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "";
			
			if (bo.getUserid() == -1) {
				stmt = "INSERT INTO BUDGET_OPERATION (flat, user, amount, date, description)"
						+ "VALUES (?, NULL, ?, ?, ?)";
				
				PreparedStatement ps = con.prepareStatement(stmt);
				ps.setInt(1, bo.getFlatid());
				
				ps.setFloat(2, bo.getAmount());
				
				java.util.Date today = new java.util.Date();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
				
				ps.setTimestamp(3, timestamp);
				
				ps.setString(4, bo.getDescription());
				
				ps.executeUpdate();
				
			} else {
				stmt = "INSERT INTO BUDGET_OPERATION (flat, user, amount, date, description)"
						+ "VALUES (?, ?, ?, ?, ?)";
				
				PreparedStatement ps = con.prepareStatement(stmt);
				
				ps.setInt(1, bo.getFlatid());
				ps.setInt(2, bo.getUserid());
				ps.setFloat(3, bo.getAmount());
				
				java.util.Date today = new java.util.Date();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
				
				ps.setTimestamp(4, timestamp);
				
				ps.setString(5, bo.getDescription());
				
				ps.executeUpdate();
			}
			
					
			return true;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
}
