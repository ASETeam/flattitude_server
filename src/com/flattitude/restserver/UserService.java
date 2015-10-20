package com.flattitude.restserver;

/** Class: UserService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of users.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.flattitude.dao.UserDAO;
import com.flattitude.dto.User;

@Path("/user")
public class UserService {
	
	  @Path("/create")
	  @POST
	  @Produces("application/json")
	  public Response register(@FormParam("email") String email,
			  	@FormParam("password") String password,
			  	@FormParam("firstname") String firstname,
			  	@FormParam("lastname") String lastname,
			  	@FormParam("phonenbr") String phone) throws JSONException {
		
		  JSONObject jsonObject = new JSONObject();
		
			try{
				//Must be removed:
				jsonObject.put("Operation", "Register");
				
				UserDAO userDAO = new UserDAO();
				
				User user = new User(email, firstname, lastname);
				user.setPhonenbr(phone);
				boolean state = userDAO.register(user, cryptWithMD5(password));
				
				if (state) jsonObject.put("success", true);
				else jsonObject.put("success", false);
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
	  
	  private String cryptWithMD5(String pass){
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] passBytes = pass.getBytes();
	        md.reset();
	        byte[] digested = md.digest(passBytes);
	        StringBuffer sb = new StringBuffer();
	        for(int i=0;i<digested.length;i++){
	            sb.append(Integer.toHexString(0xff & digested[i]));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException ex) {}
	        
	    
	    return null;
	 }
}
