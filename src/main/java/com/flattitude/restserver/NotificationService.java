package com.flattitude.restserver;

/** Class: FlatService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of flats.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import java.util.List;

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
import com.flattitude.dao.SharedObjectDAO;
import com.flattitude.dao.UserDAO;
import com.flattitude.dto.Flat;
import com.flattitude.dto.SharedObject;

@Path("/notification")
public class NotificationService {
	  private final boolean TOKEN_CTRL = false;
	  
	  @Path("/{flatid}/groupview")
	  @POST
	  @Produces("application/json")
	  public Response groupNotification(@HeaderParam("Auth") String token, 
			  @PathParam("flatid") String flatid,
			  @PathParam("timestamp") String timestamp) throws JSONException {
		  
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Group Notification");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login.");
		
			SharedObjectDAO soDAO = new SharedObjectDAO();
			
			List<SharedObject> sharedObjects = soDAO.getNotNotifiedObjects(Integer.valueOf(flatid), timestamp);
			
			boolean hasNotifications = sharedObjects.size() > 0;
			
			if (hasNotifications) {
				//Successful operation. 
				jsonObject.put("hasNotifications", true);
				JSONArray jsonArray = new JSONArray();
				
				for (SharedObject obj : sharedObjects) {
					JSONObject jsonShared = new JSONObject();
					jsonShared.put("time", obj.getTime());
					jsonShared.put("name", obj.getName());
					jsonShared.put("description", obj.getDescription());
					jsonShared.put("latitude", obj.getLatitude());
					jsonShared.put("longitude", obj.getLongitude());
					
					jsonArray.put(jsonShared);
				}
				
				jsonObject.put("sharedObject", jsonArray);
				
			} else {
				jsonObject.put("hasNotifications", false);
				
			}
			
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
}
