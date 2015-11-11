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

import com.flattitude.dao.FlatMateDAO;
import com.flattitude.dao.UserDAO;
import com.flattitude.dto.Flat;
import com.flattitude.dto.User;

@Path("/invitation")
public class InvitationService {
	private final boolean TOKEN_CTRL = false;
	
	  @Path("/create")
	  @POST	
	  @Produces("application/json")
	  public Response createInvitation(@HeaderParam("Auth") String token,
			  @FormParam("idMaster") String idMaster,
			  @FormParam("email") String email,
			  @FormParam("idFlat") String idFlat) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonProfile;
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Invitation");
			
			jsonProfile = new JSONObject();
			FlatMateDAO fmDAO = new FlatMateDAO();
			UserDAO userDAO = new UserDAO();
			
			User user = userDAO.getInfoUser(email);
			
			boolean result = fmDAO.createInvitation(user.getId(), Integer.valueOf(idFlat));
			
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			if (result) {		
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
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  
	  @Path("/consult/{idUser}")
	  @GET
	  @Produces("application/json")
	  public Response checkInvitation(@HeaderParam("Auth") String token,
			  @PathParam("idUser") String idUser) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Check Invitation");
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login.");
			
			Set<Flat> results = fmDAO.getInvitations(Integer.valueOf(idUser));
			
			JSONArray jsonArray = new JSONArray();
			
			for (Flat flat : results) {
				
				JSONObject jsonFlat = new JSONObject();
				
				jsonFlat.put("name", flat.getName());
				jsonFlat.put("country", flat.getCountry());
				jsonFlat.put("city", flat.getCity());
				jsonFlat.put("postcode", flat.getPostcode());
				jsonFlat.put("address", flat.getAddress());
				jsonFlat.put("iban", flat.getIban());
				
				jsonArray.put(jsonFlat);
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
	  public Response respondInvitation(@HeaderParam("Auth") String token,
			  @FormParam("idUser") String idUser,
			  @FormParam("idFlat") String idFlat,
			  @FormParam("accepted") boolean accepted) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Respond Invitation");
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login.");
			
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
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
}
