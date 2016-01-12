package com.flattitude.restserver;

/** Class: FlatService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of flats.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import java.util.List;
import java.util.Set;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flattitude.dao.FlatDAO;
import com.flattitude.dao.FlatMateDAO;
import com.flattitude.dao.NotificationDAO;
import com.flattitude.dao.SharedObjectDAO;
import com.flattitude.dao.UserDAO;
import com.flattitude.dto.Flat;
import com.flattitude.dto.SharedObject;
import com.flattitude.dto.Task;
import com.flattitude.notification.dto.FlatInvitationNotification;
import com.flattitude.notification.dto.ObjectPositionNotification;
import com.flattitude.notification.dto.TaskNotification;

@Path("/notification")
public class NotificationService {
	  private final boolean TOKEN_CTRL = false;
	  
	  @Path("/getNotifications/{userId}")
	  @GET
	  @Produces("application/json")
	  public Response getUserNotifications(@HeaderParam("Auth") String token,
			  @PathParam("userId") String idUser) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Get Notifications");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login.");
			
			NotificationDAO notiDAO = new NotificationDAO();
			//FlatMateDAO fmDAO = new FlatMateDAO();
			//Flat flat = fmDAO.getUserFlat(Integer.valueOf(idUser));
			
			Set<ObjectPositionNotification> objects = notiDAO.getRemainingObjectNotis(Integer.valueOf(idUser));
			
			JSONArray jsonObjects = new JSONArray();
			
			for (ObjectPositionNotification opn : objects) {			
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("type", "object_moved");
				jsonObj.put("id", opn.getNotifId());
				jsonObj.put("date", opn.getTime());
				jsonObj.put("sender", opn.getSenderId());
				jsonObj.put("object_id", opn.getObjectId());
	
				jsonObjects.put(jsonObj);
			}

			Set<FlatInvitationNotification> invitations = notiDAO.getRemainingInvitationNotis(Integer.valueOf(idUser));
			
			JSONArray jsonInvitations = new JSONArray();
			
			for (FlatInvitationNotification fin : invitations) {			
				JSONObject jsonFlat = new JSONObject();
				jsonFlat.put("type", "flat_invitation");
				jsonFlat.put("id", fin.getNotifId());
				jsonFlat.put("date", fin.getTime());
				jsonFlat.put("sender", fin.getSenderId());
				jsonFlat.put("flat_id", fin.getFlatId());
	
				jsonInvitations.put(jsonFlat);
			}
			
			Set<TaskNotification> tasks = notiDAO.getRemainingTaskNotis(Integer.valueOf(idUser));
			
			JSONArray jsonTasks = new JSONArray();
			
			for (TaskNotification task : tasks) {			
				JSONObject jsonTask = new JSONObject();
				jsonTask.put("type", "task_notification");
				jsonTask.put("id", task.getNotifId());
				jsonTask.put("date", task.getTime());
				jsonTask.put("sender", task.getSenderId());
				jsonTask.put("task_id", task.getTaskId());
	
				jsonTasks.put(jsonTask);
			}
			
			//Successful operation. 
			jsonObject.put("invitation_notifications", jsonInvitations);
			jsonObject.put("object_notifications", jsonObjects);
			jsonObject.put("task_notifications", jsonTasks);
			jsonObject.put("success", true);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  @Path("/retrievedNotification/{notifID}")
	  @GET
	  @Produces("application/json")
	  public Response ackNotification(@HeaderParam("Auth") String token,
			  @PathParam("notifID") String notifId) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Get Notifications");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login.");
			
			NotificationDAO notiDAO = new NotificationDAO();
			notiDAO.updateSeenNotification(Integer.valueOf(notifId));
			
			//Successful operation. 
			jsonObject.put("success", true);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
}
