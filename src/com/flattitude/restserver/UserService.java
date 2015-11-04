package com.flattitude.restserver;

/** Class: UserService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of users.
 *  WARNING: All operations are done without security. It MUST be implemented everywhere!
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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

import com.flattitude.dao.UserDAO;
import com.flattitude.dto.User;

@Path("/user")
public class UserService {
	private SecureRandom random = new SecureRandom();
	private final boolean TOKEN_CTRL = false;

	@Path("/create")
	@POST
	@Produces("application/json")
	public Response register(@FormParam("email") String email,
			@FormParam("password") String password,
			@FormParam("firstname") String firstname,
			@FormParam("lastname") String lastname,
			@FormParam("phonenbr") String phone) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "Register");

			UserDAO userDAO = new UserDAO();

			User user = new User(email, firstname, lastname);
			user.setPhonenbr(phone);

			int id = userDAO.register(user, cryptWithMD5(password));

			jsonObject.put("success", true);
			jsonObject.put("id", id);
			
			String token = nextSessionId();
			userDAO.updateToken(token, id);

			jsonObject.put("token", token);
		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	@Path("/login/{email}/{password}")
	@GET
	@Produces("application/json")
	public Response login(@PathParam("email") String email,
			@PathParam("password") String password) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "Login");
			UserDAO userDAO = new UserDAO();

			int id = userDAO.login(email, cryptWithMD5(password));

			if (id > -1) {
				// Successful operation.
				jsonObject.put("success", true);
				jsonObject.put("id", id);

				String token = nextSessionId();
				userDAO.updateToken(token, id);

				jsonObject.put("token", token);
			} else {
				jsonObject.put("success", false);
				jsonObject.put("reason", "email or password wrong");
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		// String result =
		// "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n"
		// + jsonObject;
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	@Path("/logout/{id}")
	@GET
	@Produces("application/json")
	public Response logout(@HeaderParam("Auth") String token, @PathParam("id") String id) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "Logout");

			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			UserDAO userDAO = new UserDAO();
			userDAO.deleteToken(id);

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

	@Path("/getinfo/{id}")
	@GET
	@Produces("application/json")
	public Response getInfo(@HeaderParam("Auth") String token, @PathParam("id") String id) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "GetInfo");
			UserDAO userDAO = new UserDAO();

			User user = userDAO.getInfoUser(Integer.valueOf(id));

			if (user != null) {
				// Successful operation.
				jsonObject.put("success", true);
				jsonObject.put("email", user.getEmail());
				jsonObject.put("firstname", user.getFirstname());
				jsonObject.put("lastname", user.getLastname());
				jsonObject.put("phonenbr", user.getPhonenbr());
				jsonObject.put("birthdate", user.getBirthdate());
				jsonObject.put("iban", user.getIban());

			} else {
				jsonObject.put("success", false);
				jsonObject.put("reason", "user does not exist");
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	@Path("/test/{id}")
	@GET
	@Produces("application/json")
	public Response testHeader(@HeaderParam("Auth") String token,
			@PathParam("id") String id) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "TestToken");

			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");


			// Successful operation.
			jsonObject.put("success", true);
			jsonObject.put("ID", id);
			jsonObject.put("Auth", token);
			
		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	private String cryptWithMD5(String pass) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
		}

		return null;
	}

	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}
}
