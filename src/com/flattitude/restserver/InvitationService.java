package com.flattitude.restserver;

/** Class: InvitationService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of invitations of users inside flats.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flattitude.dao.FlatMateDAO;
import com.flattitude.dao.UserDAO;
import com.flattitude.dto.User;

@Path("/invitation")
public class InvitationService {
	
	  @Path("/create")
	  @POST	
	  @Produces("application/json")
	  public Response createInvitation(@FormParam("idUser") String idUser,
			  @FormParam("idFlat") String idFlat) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonProfile;
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Invitation");
			
			jsonProfile = new JSONObject();
			FlatMateDAO fmDAO = new FlatMateDAO();
			
			boolean result = fmDAO.createInvitation(Integer.valueOf(idUser), Integer.valueOf(idFlat));
			
			if (result) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.getInfoUser(Integer.valueOf(idUser));
				
				jsonProfile.put("firstname", user.getFirstname());
				jsonProfile.put("lastname", user.getLastname());
				jsonProfile.put("email", user.getEmail());
			}
			
			//Successful operation. 
			jsonObject.put("success", true);
			jsonObject.put("profile", jsonProfile);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			
			jsonObject.put("reason", sw.toString());
			jsonObject.put("idFlat", idFlat);
			jsonObject.put("idUser", idUser);
		}
		

		//String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  
	  @Path("/consult/{idUser}")
	  @GET
	  @Produces("application/json")
	  public Response checkInvitation(@PathParam("idUser") String idUser) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Check Invitation");
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			Map<String, String> results = fmDAO.getInvitations(Integer.valueOf(idUser));
			
			JSONArray jsonArray = new JSONArray();
			
			for (String key : results.keySet()) {
				jsonArray.put(results.get(key));
			}
			
			//Successful operation. 
			jsonObject.put("invitations", jsonArray);
			jsonObject.put("success", true);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		

		//String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  @Path("/respond")
	  @POST
	  @Produces("application/json")
	  public Response respondInvitation(@FormParam("idUser") String idUser,
			  @FormParam("idFlat") String idFlat,
			  @FormParam("accepted") boolean accepted) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Respond Invitation");
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			
			//Successful operation. 
			boolean result;
			if (accepted) {
				result = fmDAO.acceptInvitation(Integer.valueOf(idUser), Integer.valueOf(idFlat));
			} else {
				result = fmDAO.deleteInvitation(Integer.valueOf(idUser), Integer.valueOf(idFlat));
			}
			
			jsonObject.put("success", result);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		

		//String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
}
