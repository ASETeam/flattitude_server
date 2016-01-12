package com.flattitude.restserver;

/** Class: BudgetService.java
 *  Author: Flattitude Team.
 *  
 *  Service dedicated to the management of the common budget.
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flattitude.dao.BudgetDAO;
import com.flattitude.dao.FlatDAO;
import com.flattitude.dao.FlatMateDAO;
import com.flattitude.dao.UserDAO;
import com.flattitude.dto.BudgetOperation;
import com.flattitude.dto.Flat;

@Path("/budget")
public class BudgetService {
	private final boolean TOKEN_CTRL = false;

	@Path("/balances")
	@GET
	@Produces("application/json")
	public Response getBalance(@HeaderParam("Auth") String token,
			@PathParam("flatid") String flatid, 
			@PathParam("userid") String userid) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		try {

			// Must be removed:
			jsonObject.put("Operation", "Budget Retrieval");

			FlatDAO flatDAO = new FlatDAO();
			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			float flatBalance = flatDAO.getFlatBalance(Integer.valueOf(flatid));
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			
			float personalBalance = fmDAO.getUserBalance(Integer.valueOf(userid));
			
			jsonObject.put("flat_balance", flatBalance);
			jsonObject.put("personal_balance", personalBalance);
			
		} catch (Exception ex) {
			jsonObject.put("success", false);

			// Manage errors properly.
			jsonObject.put("reason", ex.getMessage());
		}

		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	@Path("/history")
	@GET
	@Produces("application/json")
	public Response getHistoricBalance(@HeaderParam("Auth") String token,
			@PathParam("flatid") String flatid, 
			@PathParam("userid") String userid) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		try {

			// Must be removed:
			jsonObject.put("Operation", "Budget Historic Retrieval");

			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			JSONArray array = new JSONArray();
			
			BudgetDAO bmDAO = new BudgetDAO();
			
			List<BudgetOperation> operations = bmDAO.getFlatHistoric(Integer.valueOf(flatid));
			
			for (BudgetOperation bo : operations) {
				JSONObject jsonBudget = new JSONObject();
				jsonBudget.put("id", bo.getId());
				jsonBudget.put("flatid", bo.getFlatid());
				jsonBudget.put("userid", bo.getUserid());
				jsonBudget.put("amount", bo.getAmount());
				jsonBudget.put("date", bo.getDate());
				jsonBudget.put("description", bo.getDescription());
				
				array.put(jsonObject);
			}
			
			jsonObject.put("historic", array);
			
			String result = jsonObject.toString();
			return Response.status(200).entity(result).build();
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

	@Path("/put")
	@POST
	@Produces("application/json")
	public Response putMoney(@HeaderParam("Auth") String token,
			@FormParam("userid") String userid,
			@FormParam("flatid") String flatid,
			@FormParam("amount") String amount,
			@FormParam("description") String description) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "Put money budget");

			FlatDAO flatDAO = new FlatDAO();
			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			float flatBalance = flatDAO.getFlatBalance(Integer.valueOf(flatid));
			flatDAO.modifyBalance(Integer.valueOf(flatid), flatBalance+Float.valueOf(amount));
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			
			float personalBalance = fmDAO.getUserBalance(Integer.valueOf(userid));
			fmDAO.modifyBalance(Integer.valueOf(userid), personalBalance+Float.valueOf(amount));
			
			
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
	
	@Path("/pay")
	@POST
	@Produces("application/json")
	public Response payMoney(@HeaderParam("Auth") String token,
			@FormParam("userid") String userid,
			@FormParam("flatid") String flatid,
			@FormParam("amount") String amount,
			@FormParam("description") String description) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();

		try {
			// Must be removed:
			jsonObject.put("Operation", "Put money budget");

			FlatDAO flatDAO = new FlatDAO();
			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			float flatBalance = flatDAO.getFlatBalance(Integer.valueOf(flatid));
			flatDAO.modifyBalance(Integer.valueOf(flatid), flatBalance-Float.valueOf(amount));
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			int totalMembers = fmDAO.getFlatMembers(Integer.valueOf(flatid)).size();
			
			float personalBalance = fmDAO.getUserBalance(Integer.valueOf(userid));
			fmDAO.modifyBalance(Integer.valueOf(userid), personalBalance-(Float.valueOf(amount)/totalMembers));
			
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
}
