package com.flattitude.restserver;

/** Class: FlatService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of flats.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.flattitude.dao.FlatDAO;
import com.flattitude.dto.Flat;

@Path("/flat")
public class FlatService {
	
	  @Path("/create")
	  @POST
	  @Produces("application/json")
	  public Response createFlat(@FormParam("name") String name,
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
			
			Flat flat = new Flat(name, country, address, postcode, city, IBAN);
			FlatDAO flatDAO = new FlatDAO();
			
			int id = flatDAO.create(flat);
			
			if (id > -1) {
				//Successful operation. 
				jsonObject.put("success", true);
				jsonObject.put("id", id);
			} else {
				jsonObject.put("success", false);
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			
			//Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}
		

		//String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	  }
	  
	  
	  @Path("/quit")
	  @GET
	  @Produces("application/json")
	  public Response quitFlat() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "QuitFlat");
			
			
			
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
