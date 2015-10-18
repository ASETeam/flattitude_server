package com.flattitude.restserver;

/** Class: UserService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of users.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/user")
public class UserService {
	
	  @Path("/create")
	  @GET
	  @Produces("application/json")
	  public Response register() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Register");
			
			
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
	  
	  
	  @Path("/login/{email}/{password}")
	  @GET
	  @Produces("application/json")
	  public Response login(@PathParam("email") String email, @PathParam("password") String password) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Login");
			
			
			
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
	  
	  @Path("/logout/{email}")
	  @GET
	  @Produces("application/json")
	  public Response logout() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Logout");
			
			
			
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
