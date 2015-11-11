package com.flattitude.restserver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
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
import com.flattitude.dto.User;

@Path("/sharedobjects")
public class SharedObjectService {
	private final boolean TOKEN_CTRL = false;
	
	  @Path("/create")
	  @POST	
	  @Produces("application/json")
	  public Response createInvitation(@HeaderParam("Auth") String token,
			  @FormParam("idFlat") String idFlat,
			  @FormParam("name") String name,
			  @FormParam("desc") String desc,
			  @FormParam("latitude") float latitude,
			  @FormParam("longitude") float longitude) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Shared Object");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			SharedObjectDAO soDAO = new SharedObjectDAO();

			SharedObject object = new SharedObject();
			object.setFlatID(Integer.valueOf(idFlat));
			object.setName(name);
			object.setDescription(desc);
			object.setLatitude(latitude);
			object.setLongitude(longitude);
			object.setTime(new Date(System.currentTimeMillis()));
			
			int idObject = soDAO.addSharedObject(object);
			
			if (idObject > 0) {				
				
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
	
	
}
