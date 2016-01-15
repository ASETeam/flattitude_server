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
import com.flattitude.dto.User;

@Path("/budget")
public class BudgetService {
	private final boolean TOKEN_CTRL = false;

	@Path("/balances/get")
	@POST
	@Produces("application/json")
	public Response getBalance(@HeaderParam("Auth") String token,
			@FormParam("flatid") String flatid, 
			@FormParam("userid") String userid) throws JSONException {

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
	@POST
	@Produces("application/json")
	public Response getHistoricBalance(@HeaderParam("Auth") String token,
			@FormParam("flatid") String flatid, 
			@FormParam("userid") String userid) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		try {

			// Must be removed:
			jsonObject.put("Operation", "Budget Historic Retrieval");

			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			JSONArray array = new JSONArray();
			BudgetDAO bmDAO = new BudgetDAO();
			UserDAO userDAO = new UserDAO();
			
			List<BudgetOperation> operations = bmDAO.getFlatHistoric(Integer.valueOf(flatid));
			
			for (BudgetOperation bo : operations) {
				JSONObject jsonBudget = new JSONObject();
				jsonBudget.put("id", bo.getId());
				jsonBudget.put("flatid", bo.getFlatid());
				
				if (bo.getUserid() != -1) {
					User user = userDAO.getInfoUser(bo.getUserid());
					jsonBudget.put("userName", user.getLastname());
				} else {
					jsonBudget.put("userName", "");
				}
				
				jsonBudget.put("amount", bo.getAmount());
				jsonBudget.put("date", bo.getDate());
				jsonBudget.put("description", bo.getDescription());
				
				array.put(jsonBudget);
			}
			
			jsonObject.put("historic", array);
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
			
			BudgetOperation bo = new BudgetOperation ();
			bo.setFlatid(Integer.valueOf(flatid));
			
			if (userid == null) bo.setUserid(-1);
			else bo.setUserid(Integer.valueOf(userid));
			
			bo.setAmount(Float.valueOf(amount));
			bo.setDescription(description);
			
			BudgetDAO bdDAO = new BudgetDAO();
			bdDAO.createBudgetOperation(bo);
			
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
			jsonObject.put("Operation", "Take out money from budget");

			FlatDAO flatDAO = new FlatDAO();
			// if (TOKEN_CTRL && userDAO.checkToken(token)) throw new
			// Exception("Token not valid. Please login.");

			BudgetOperation bo = new BudgetOperation ();
			bo.setFlatid(Integer.valueOf(flatid));
			bo.setUserid(Integer.valueOf(userid));
			bo.setAmount(-Float.valueOf(amount));
			bo.setDescription(description);
			
			BudgetDAO bdDAO = new BudgetDAO();
			bdDAO.createBudgetOperation(bo);
			
			float flatBalance = flatDAO.getFlatBalance(Integer.valueOf(flatid));
			flatDAO.modifyBalance(Integer.valueOf(flatid), flatBalance-Float.valueOf(amount));
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			
			//Pay equity.
			float money = Float.valueOf(amount) * 100;
			List<Integer> members = fmDAO.getFlatMembers(Integer.valueOf(flatid));

			float rest = Math.round(money % members.size());
			float amountExact = ((money - rest)/members.size()) / 100;
			float amountExactRested = (float) Math.floor((money - rest)/members.size() + rest)*100 ;
			
			int i = 0;
			for (i = 0; i < members.size()-1; i++) {
				float personalBalance = fmDAO.getUserBalance(members.get(i));
				fmDAO.modifyBalance(Integer.valueOf(members.get(i)), personalBalance-amountExact);	
			}
			
			float personalBalance = fmDAO.getUserBalance(members.get(i));
			fmDAO.modifyBalance(members.get(i), personalBalance-amountExactRested);	
			
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
