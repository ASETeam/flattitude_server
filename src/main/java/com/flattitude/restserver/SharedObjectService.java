package com.flattitude.restserver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import com.flattitude.dto.User;
import com.flattitude.notification.dto.ObjectPositionNotification;

@Path("/sharedobjects")
public class SharedObjectService {
	private final boolean TOKEN_CTRL = false;
	
	  @Path("/get/{flatId}")
	  @GET	
	  @Produces("application/json")
	  public Response getFlatObjects(@HeaderParam("Auth") String token,
			  @PathParam("flatId") String flatId) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Shared Object");
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			SharedObjectDAO soDAO = new SharedObjectDAO();

			SharedObject object = new SharedObject();
			JSONArray arrayObjects = new JSONArray();
			List<SharedObject> objects = soDAO.getFlatObjects(Integer.valueOf(flatId));
			
			for (SharedObject sobject : objects) {
				JSONObject jsonSharedObject = new JSONObject();
				jsonSharedObject.put("id", sobject.getID());
				jsonSharedObject.put("user", sobject.getUserID());
				jsonSharedObject.put("flat", sobject.getFlatID());
				jsonSharedObject.put("name", sobject.getName());
				jsonSharedObject.put("description", sobject.getDescription());
				jsonSharedObject.put("latitude", sobject.getLatitude());
				jsonSharedObject.put("longitude", sobject.getLongitude());
				jsonSharedObject.put("time", sobject.getTime());
				
				arrayObjects.put(jsonSharedObject);
			}
			
			
			//Successful operation. 
			jsonObject.put("success", true);
			jsonObject.put("objects", arrayObjects);				
	
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			
			jsonObject.put("reason", sw.toString());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	
	  @Path("/create")
	  @POST	
	  @Produces("application/json")
	  public Response createSharedObject(@HeaderParam("Auth") String token,
			  @FormParam("userid") String idUser,
			  @FormParam("flatid") String idFlat,
			  @FormParam("objectname") String name,
			  @FormParam("objectdescription") String desc,
			  @FormParam("lat") float latitude,
			  @FormParam("long") float longitude) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Shared Object");
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			SharedObjectDAO soDAO = new SharedObjectDAO();

			SharedObject object = new SharedObject();
			object.setUserID(Integer.valueOf(idUser));
			object.setFlatID(Integer.valueOf(idFlat));
			object.setName(name);
			object.setDescription(desc);
			object.setLatitude(latitude);
			object.setLongitude(longitude);
			object.setTime(new Date(System.currentTimeMillis()));
			
			int idObject = soDAO.addSharedObject(object);
			
			if (idObject > 0) {		
				NotificationDAO notiDAO = new NotificationDAO();
				int notId = notiDAO.createGenericNotification(Integer.valueOf(idUser));
				notiDAO.createObjectPositionNotification(notId, Integer.valueOf(idObject));
				
				List<Integer> destinators = fmDAO.getFlatMembers(Integer.valueOf(idFlat));
				notiDAO.createFlatUsersNotification(destinators, notId);
				
				//Successful operation. 
				jsonObject.put("success", true);
				jsonObject.put("idObject", idObject);				
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			
			jsonObject.put("reason", sw.toString());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	
	  
	  
	  @Path("/edit")
	  @POST	
	  @Produces("application/json")
	  public Response editSharedObject(@HeaderParam("Auth") String token,
			  @FormParam("objectid") String objectId,
			  @FormParam("name") String name,
			  @FormParam("desc") String desc,
			  @FormParam("lat") float latitude,
			  @FormParam("long") float longitude) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Shared Object");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			SharedObjectDAO soDAO = new SharedObjectDAO();

			SharedObject object = new SharedObject();
			object.setID(Integer.valueOf(objectId));
			object.setName(name);
			object.setDescription(desc);
			object.setLatitude(latitude);
			object.setLongitude(longitude);
			object.setTime(new Date(System.currentTimeMillis()));
			
			soDAO.editSharedObject(object);
			jsonObject.put("success", true);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			
			jsonObject.put("reason", sw.toString());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  @Path("/check/{userid}")
	  @POST	
	  @Produces("application/json")
	  public Response checkSharedObject(@HeaderParam("Auth") String token,
			  @PathParam("userid") String idUser) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Shared Object");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			SharedObjectDAO soDAO = new SharedObjectDAO();

			Set<SharedObject> objects = soDAO.getUserNotNotifiedObjects(Integer.valueOf(idUser));
			
			JSONArray jsonObjects = new JSONArray();
			
			for (SharedObject obj : objects) {			
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("objectid", obj.getID());
				jsonObj.put("long", obj.getLongitude());
				jsonObj.put("lat", obj.getLatitude());
				jsonObj.put("objectname", obj.getName());
				jsonObj.put("objectdescription", obj.getDescription());
	
				jsonObjects.put(jsonObj);
			}
			
			jsonObject.put("success", true);
			jsonObject.put("objects", jsonObjects);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			
			jsonObject.put("reason", sw.toString());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  @Path("/delete/{object}") 
	  @GET	
	  @Produces("application/json")
	  public Response deleteSharedObject(@HeaderParam("Auth") String token,
			  @PathParam("object") String objectId) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Shared Object");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			SharedObjectDAO soDAO = new SharedObjectDAO();
			soDAO.quitSharedObject(Integer.valueOf(objectId));
				
			//Successful operation. 
			jsonObject.put("success", true);				

		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			
			jsonObject.put("reason", sw.toString());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
}
