package com.flattitude.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flattitude.notification.dto.FlatInvitationNotification;
import com.flattitude.notification.dto.ObjectPositionNotification;
import com.flattitude.notification.dto.TaskNotification;

public class NotificationDAO {
	
	public int createGenericNotification (int senderId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "INSERT INTO NOTIFICATION (SENDER_ID, TIME) VALUES (?, ?) ";
			
			PreparedStatement ps = con.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, senderId);
			
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
			
			ps.setTimestamp(2, timestamp);
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			int notifId = rs.getInt(1);
			
			return notifId;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
	
	public void createFlatInvitationNotification (int notId, int flatId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "INSERT INTO FLAT_INVITATION_NOTIFICATION (ID, FLAT_ID) VALUES (?, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, notId);
			ps.setInt(2, flatId);
			
			ps.executeUpdate();
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
	
	public void createObjectPositionNotification (int notId, int objectId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "INSERT INTO OBJECT_POSITION_NOTIFICATION (ID, OBJECT_ID) VALUES (?, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, notId);
			ps.setInt(2, objectId);
			
			ps.executeUpdate();
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}	
	
	public void createUserNotification (int user_id, int notification_id) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "INSERT INTO USER_NOTIFICATION (USER_ID, NOTIFICATION_ID, SEEN_DATE, HAS_BEEN_RETRIEVED) VALUES (?, ?, NULL, FALSE)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, user_id);
			ps.setInt(2, notification_id);
			
			ps.executeUpdate();
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
	
	public void createFlatUsersNotification (List<Integer> users, int notification_id) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			for (int user : users) {
				String stmt = "INSERT INTO USER_NOTIFICATION (USER_ID, NOTIFICATION_ID, SEEN_DATE, HAS_BEEN_RETRIEVED) VALUES (?, ?, NULL, FALSE)";
				
				PreparedStatement ps = con.prepareStatement(stmt);
				ps.setInt(1, user);
				ps.setInt(2, notification_id);
				
				ps.executeUpdate();
			}
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}

	public Set<ObjectPositionNotification> getRemainingObjectNotis(int userId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT NOTIFICATION_ID, SENDER_ID, TIME, OBJECT_ID FROM USER_NOTIFICATION "
					+ "INNER JOIN NOTIFICATION ON NOTIFICATION.ID = NOTIFICATION_ID "
					+ "INNER JOIN OBJECT_POSITION_NOTIFICATION ON OBJECT_POSITION_NOTIFICATION.ID = NOTIFICATION_ID "
					+ "WHERE USER_NOTIFICATION.USER_ID = ? AND USER_NOTIFICATION.SEEN_DATE IS NULL";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, userId);
			
			ResultSet rs = ps.executeQuery();
			
			Set<ObjectPositionNotification> results = new HashSet<ObjectPositionNotification>();
			
			while (rs.next()) {
				ObjectPositionNotification opn = new ObjectPositionNotification(
						rs.getInt(1),
						rs.getInt(2),
						rs.getDate(3),
						rs.getInt(4));
				
				results.add(opn);
			}
			
			return results;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	} 
	
	public Set<FlatInvitationNotification> getRemainingInvitationNotis(int userId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT NOTIFICATION_ID, SENDER_ID, TIME, FLAT_ID FROM USER_NOTIFICATION "
					+ "INNER JOIN NOTIFICATION ON NOTIFICATION.ID = NOTIFICATION_ID "
					+ "INNER JOIN FLAT_INVITATION_NOTIFICATION ON FLAT_INVITATION_NOTIFICATION.ID = NOTIFICATION_ID "
					+ "WHERE USER_NOTIFICATION.USER_ID = ? AND USER_NOTIFICATION.SEEN_DATE IS NULL";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, userId);
			
			ResultSet rs = ps.executeQuery();
			
			Set<FlatInvitationNotification> results = new HashSet<FlatInvitationNotification>();
			
			while (rs.next()) {
				FlatInvitationNotification opn = new FlatInvitationNotification(
						rs.getInt(1),
						rs.getInt(2),
						rs.getDate(3),
						rs.getInt(4));
				
				results.add(opn);
			}
			
			return results;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
	
	
	public void updateSeenNotification(int notifId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
						
			String stmt = "UPDATE USER_NOTIFICATION SET USER_NOTIFICATION.SEEN_DATE = TRUE WHERE USER_NOTIFICATION.USER_ID = ?";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, notifId);
			
			ps.executeUpdate();
		
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}

	public void createTaskNotification(int notId, Integer taskId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "INSERT INTO TASK_NOTIFICATION (ID, TASK_ID) VALUES (?, ?)";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, notId);
			ps.setInt(2, taskId);
			
			ps.executeUpdate();
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}

	public Set<TaskNotification> getRemainingTaskNotis(int userId) throws Exception {
		try {
			Connection con = new Database().Get_Connection();
			
			String stmt = "SELECT NOTIFICATION_ID, SENDER_ID, TIME, TASK_ID FROM USER_NOTIFICATION "
					+ "INNER JOIN NOTIFICATION ON NOTIFICATION.ID = NOTIFICATION_ID "
					+ "INNER JOIN TASK_NOTIFICATION ON TASK_NOTIFICATION.ID = NOTIFICATION_ID "
					+ "WHERE USER_NOTIFICATION.USER_ID = ? AND USER_NOTIFICATION.SEEN_DATE IS NULL";
			
			PreparedStatement ps = con.prepareStatement(stmt);
			ps.setInt(1, userId);
			
			ResultSet rs = ps.executeQuery();
			
			Set<TaskNotification> results = new HashSet<TaskNotification>();
			
			while (rs.next()) {
				TaskNotification opn = new TaskNotification(
						rs.getInt(1),
						rs.getInt(2),
						rs.getDate(3),
						rs.getInt(4));
				
				results.add(opn);
			}
			
			return results;
		} catch (Exception sqlex) {
			throw sqlex;
		}
	}
	
}
