package com.flattitude.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.flattitude.dto.Task;
import com.mysql.jdbc.Statement;

public class TaskDAO {
	
	public int addTask (Task task) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "INSERT INTO TASK (author, flat, type, description, date, duration) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, task.getUserID());
			ps.setInt(2, task.getFlatID());
			ps.setInt(3, task.getType());
			ps.setString(4, task.getDescription());
			
			java.sql.Timestamp timestamp = new java.sql.Timestamp(task.getTime().getTime());
			ps.setTimestamp(5, timestamp);
			
			ps.setInt(6, task.getDuration());
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			int idObject = rs.getInt(1);
						
			return idObject;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public boolean editTask (Task task) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			if (task.getDescription() != null || task.getType() != -1) {
				String stmt = "UPDATE TASK SET ";
				
				int countPars = 1;
				
				if (task.getDescription() != null) {stmt += " DESCRIPTION = '" + task.getDescription() + "'"; countPars++;}
				if (countPars > 1 ) stmt += ", ";
				if (task.getType() != -1) {stmt += " TYPE = " + task.getType(); countPars++;}
				if (countPars > 1 ) stmt += ", ";
				if (task.getTime() != null) {stmt += " DATE = '" + new java.sql.Timestamp(task.getTime().getTime()) + "'"; countPars++; }
				if (countPars > 1 ) stmt += ", ";
				if (task.getDuration() != -1) {stmt += " DURATION = " + task.getDuration(); countPars++; }
				
				stmt += " WHERE ID = " + task.getTaskID();
				
				PreparedStatement ps = con.prepareStatement(stmt);
				ps.executeUpdate();
			}
						
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public boolean quitTask (int idTask) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "DELETE FROM TASK WHERE ID = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, idTask);
			
			ps.executeUpdate();
			
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public List<Task> getNotNotifiedTasks(Date beginDate, Date endDate) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM TASK WHERE TIME > ? AND TIME < ? ";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setDate(1, new java.sql.Date(beginDate.getTime()));
			ps.setDate(2, new java.sql.Date(endDate.getTime()));
			
			ResultSet rs = ps.executeQuery();
			
			List<Task> tasks = new ArrayList<Task>();
						
			Task task;
			
			while (rs.next()) {
				task = new Task();
				task.setTaskID(rs.getInt("id"));
				task.setFlatID(rs.getInt("flat"));
				task.setUserID(rs.getInt("author"));
				task.setDescription(rs.getString("DESCRIPTION"));
				task.setType(rs.getInt("TYPE"));
				task.setTime(rs.getDate("DATE"));
				task.setDuration(rs.getInt("DURATION"));
				tasks.add(task);
			}
			
			return tasks;
			
		} catch (Exception ex) {
			throw ex;
		}
		
	}

	public List<Task> getFlatTasks(Integer flatId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			String stmt = "SELECT * FROM TASK WHERE FLAT = ? ";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			
			ps.setInt(1, flatId);
			
			ResultSet rs = ps.executeQuery();
			
			List<Task> tasks = new ArrayList<Task>();
						
			Task task;
			
			while (rs.next()) {
				task = new Task();
				task.setTaskID(rs.getInt("id"));
				task.setFlatID(rs.getInt("flat"));
				task.setUserID(rs.getInt("author"));
				task.setDescription(rs.getString("DESCRIPTION"));
				task.setType(rs.getInt("TYPE"));
				task.setTime(rs.getTimestamp("DATE"));
				task.setDuration(rs.getInt("DURATION"));
				tasks.add(task);
			}
			
			return tasks;
			
		} catch (Exception ex) {
			throw ex;
		}
	}
	

}
