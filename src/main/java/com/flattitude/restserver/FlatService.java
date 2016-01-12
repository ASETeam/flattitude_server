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
	public Response createFlat(@FormParam("name") String name,
			@FormParam("country") String country,
			@FormParam("address") String address,
			@FormParam("postcode") String postcode,
			@FormParam("city") String city, @FormParam("IBAN") String IBAN,
			@FormParam("masterid") String masterid) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		try {

			// Must be removed:
			jsonObject.put("Operation", "Flat Creation");

			UserDAO userDAO = new UserDAO();
			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			Flat flat = new Flat(name, country, address, postcode, city, IBAN);
			FlatDAO flatDAO = new FlatDAO();

			int idFlat = flatDAO.create(flat);

			// Assign to created Flat.
			FlatMateDAO fmDAO = new FlatMateDAO();
			fmDAO.assignFlat(Integer.valueOf(masterid), idFlat, true);

			// Chat group creation
			//createGroupChat(Integer.valueOf(idFlat).toString() + name, name);
			//assignUserGroupChat(Integer.valueOf(idFlat).toString() + name, masterid);
			
			if (idFlat > -1) {
				// Successful operation.
				jsonObject.put("success", true);
				jsonObject.put("id", idFlat);
			} else {
				jsonObject.put("success", false);
			}

		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	@Path("/info/{flatid}")
	@GET
	@Produces("application/json")
	public Response getInfoFlat(@HeaderParam("Auth") String token,
			@PathParam("flatid") String flatid) {
		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "Get Flat");
			UserDAO userDAO = new UserDAO();
			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			FlatDAO flatDAO = new FlatDAO();
			Flat flat = flatDAO.getInfo(flatid);
			JSONObject jsonFlat = new JSONObject();

			jsonFlat.put("name", flat.getName());
			jsonFlat.put("country", flat.getCountry());
			jsonFlat.put("city", flat.getCity());
			jsonFlat.put("postcode", flat.getPostcode());
			jsonFlat.put("address", flat.getAddress());
			jsonFlat.put("iban", flat.getIban());

			// Successful operation.
			jsonObject.put("success", true);
			jsonObject.put("flat", jsonFlat);
		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	@Path("/userflat/{userid}")
	@GET
	@Produces("application/json")
	public Response getUserInfoFlat(@HeaderParam("Auth") String token,
			@PathParam("userid") String userid) {
		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "User's Flat Info");
			UserDAO userDAO = new UserDAO();
			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			FlatMateDAO flatMateDAO = new FlatMateDAO();
			Flat flat = flatMateDAO.getUserFlat(Integer.valueOf(userid));

			if (flat != null) {
				JSONObject jsonFlat = new JSONObject();
				jsonFlat.put("flatid", flat.getID());
				jsonFlat.put("name", flat.getName());
				jsonFlat.put("country", flat.getCountry());
				jsonFlat.put("city", flat.getCity());
				jsonFlat.put("postcode", flat.getPostcode());
				jsonFlat.put("address", flat.getAddress());
				jsonFlat.put("iban", flat.getIban());

				// Successful operation.
				jsonObject.put("success", true);
				jsonObject.put("flat", jsonFlat);
			} else
				throw new IllegalArgumentException(
						"The user is not living in any flat.");

		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	@Path("/quit")
	@POST
	@Produces("application/json")
	public Response quitFlat(@HeaderParam("Auth") String token,
			@FormParam("idUser") String idUser,
			@FormParam("idFlat") String idFlat) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "QuitFlat");

			FlatMateDAO flatMateDAO = new FlatMateDAO();
			flatMateDAO.quitFlat(Integer.valueOf(idUser), Integer.valueOf(idFlat));
			
		
			// Successful operation.
			jsonObject.put("success", true);
		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	/*private void createGroupChat(String nameRoom, String flat) {
		// Set Shared secret key
		AuthenticationToken authenticationToken = new AuthenticationToken(
				"admin", "GRT1234");

		// Set Openfire settings (9090 is the port of Openfire Admin Console)
		RestApiClient restApiClient = new RestApiClient(
				"http://ec2-54-218-39-214.us-west-2.compute.amazonaws.com",
				9090, authenticationToken);

		MUCRoomEntity chatRoom = new MUCRoomEntity(nameRoom, flat,
				"Chat for flat " + flat);
		
		restApiClient.createChatRoom(chatRoom);
	}

	private void assignUserGroupChat(String roomName, String user) {
		// Set Shared secret key
		AuthenticationToken authenticationToken = new AuthenticationToken(
				"admin", "GRT1234");

		// Set Openfire settings (9090 is the port of Openfire Admin Console)
		RestApiClient restApiClient = new RestApiClient(
				"http://ec2-54-218-39-214.us-west-2.compute.amazonaws.com",
				9090, authenticationToken);
		
		MUCRoomEntity chatRoom = restApiClient.getChatRoom(roomName);
		List<String> members = chatRoom.getMembers();
		members.add(user);
		chatRoom.setMembers(members);
	}
	
	private void deleteUserGroupChat(String roomName, String user) {
		// Set Shared secret key
		AuthenticationToken authenticationToken = new AuthenticationToken(
				"admin", "GRT1234");

		// Set Openfire settings (9090 is the port of Openfire Admin Console)
		RestApiClient restApiClient = new RestApiClient(
				"http://ec2-54-218-39-214.us-west-2.compute.amazonaws.com",
				9090, authenticationToken);
		
		MUCRoomEntity chatRoom = restApiClient.getChatRoom(roomName);
		List<String> members = chatRoom.getMembers();
		members.remove(user);
		chatRoom.setMembers(members);
	}*/
}
