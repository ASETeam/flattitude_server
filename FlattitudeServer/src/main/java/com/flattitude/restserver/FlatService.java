package com.flattitude.restserver;

/** Class: FlatService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of flats.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.flattitude.dao.FlatDAO;
import com.flattitude.dao.FlatMateDAO;
import com.flattitude.dao.UserDAO;
import com.flattitude.dto.Flat;

@Path("/flat")
public class FlatService {
	  private final boolean TOKEN_CTRL = false;
	  
	  @Path("/create")
	  @POST
	  @Produces("application/json")
	  public Response createFlat(
			  @FormParam("name") String name,
			  @FormParam("country") String country,
			  @FormParam("address") String address,
			  @FormParam("postcode") String postcode,
			  @FormParam("city") String city,
			  @FormParam("IBAN") String IBAN,
			  @FormParam("masterid") String masterid ) throws JSONException {
		  
		JSONObject jsonObject = new JSONObject();
		
		try{
			
			//Must be removed:
			jsonObject.put("Operation", "Flat Creation");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login.");
			
			Flat flat = new Flat(name, country, address, postcode, city, IBAN);
			FlatDAO flatDAO = new FlatDAO();
			
			int idFlat = flatDAO.create(flat);
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			fmDAO.assignFlat (Integer.valueOf(masterid), idFlat, true);
			
			if (idFlat > -1) {
				//Successful operation. 
				jsonObject.put("success", true);
				jsonObject.put("id", idFlat);
			} else {
				jsonObject.put("success", false);
			}
			
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  @Path("/info/{flatid}")
	  @GET
	  @Produces("application/json") 
	  public Response getInfoFlat(@HeaderParam("Auth") String token, @PathParam("flatid") String flatid) {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "QuitFlat");
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login.");
				
			FlatDAO flatDAO = new FlatDAO();
			Flat flat = flatDAO.getInfo(flatid);
			JSONObject jsonFlat = new JSONObject();
			
			jsonFlat.put("name", flat.getName());
			jsonFlat.put("country", flat.getCountry());
			jsonFlat.put("city", flat.getCity());
			jsonFlat.put("postcode", flat.getPostcode());
			jsonFlat.put("address", flat.getAddress());
			jsonFlat.put("iban", flat.getIban());
			
			
			//Successful operation. 
			jsonObject.put("success", true);
			jsonObject.put("flat", jsonFlat);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  @Path("/quit")
	  @GET
	  @Produces("application/json")
	  public Response quitFlat(@HeaderParam("Auth") String token) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "QuitFlat");
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login.");
						
			
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
