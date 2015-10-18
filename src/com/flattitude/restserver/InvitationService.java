package com.flattitude.restserver;

/** Class: InvitationService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of invitations of users inside flats.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/invitation")
public class InvitationService {
	
	  @Path("/create")
	  @GET
	  @Produces("application/json")
	  public Response createInvitation() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonProfile;
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Invitation");
			
			jsonProfile = new JSONObject();
			jsonProfile.put("firstname", "firstname");
			jsonProfile.put("lastname", "lastname");
			jsonProfile.put("email", "email");
			
			//Successful operation. 
			
			jsonObject.put("success", true);
			jsonObject.put("profile", jsonProfile);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		

		//String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  
	  @Path("/consult/{email}")
	  @GET
	  @Produces("application/json")
	  public Response checkInvitation(@PathParam("email") String email) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Check Invitation");
			
			
			
			//Successful operation. 
			jsonObject.put("invitations", "");
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
	  @GET
	  @Produces("application/json")
	  public Response respondInvitation() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Respond Invitation");
			
			//Successful operation. 
			
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
	  
}
