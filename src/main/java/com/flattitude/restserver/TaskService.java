package com.flattitude.restserver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import com.flattitude.dao.FlatMateDAO;
import com.flattitude.dao.NotificationDAO;
import com.flattitude.dao.TaskDAO;
import com.flattitude.dao.UserDAO;
import com.flattitude.dto.Flat;
import com.flattitude.dto.Task;

@Path("/tasks")
public class TaskService {
	private final boolean TOKEN_CTRL = false;
	
	  @Path("/get/{flatId}")
	  @GET
	  @Produces("application/json") 
	  public Response getFlatTasks(@HeaderParam("Auth") String token,
			  @PathParam("flatId") String flatId) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Shared Object");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			TaskDAO taskDAO = new TaskDAO();
			List<Task> tasks = taskDAO.getFlatTasks(Integer.valueOf(flatId));
			
			JSONArray jsonTasks = new JSONArray();
			
			for (Task task : tasks) {
				JSONObject jsonTask = new JSONObject();
				jsonTask.put("id", task.getTaskID());
				jsonTask.put("author", task.getUserID());
				jsonTask.put("date", task.getTime());
				jsonTask.put("description", task.getDescription());
				jsonTask.put("flat", task.getFlatID());
				jsonTask.put("duration", task.getDuration());
				jsonTask.put("type", task.getType());
				
				jsonTasks.put(jsonTask);
			}
			
			//Successful operation. 
			jsonObject.put("success", true);				
			jsonObject.put("tasks", jsonTasks);
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
	
	
	  @Path("/get")
	  @POST	
	  @Produces("application/json")
	  public Response getTasks(@HeaderParam("Auth") String token,
			  @FormParam("userid") String idUser,
			  @FormParam("flatid") String idFlat,
			  @FormParam("beginYear") String beginYear,
			  @FormParam("beginMonth") String beginMonth,
			  @FormParam("beginDay") String beginDay,
			  @FormParam("beginHour") String beginHour,
			  @FormParam("beginMinute") String beginMinute,
			  @FormParam("endYear") String endYear,
			  @FormParam("endMonth") String endMonth,
			  @FormParam("endDay") String endDay,
			  @FormParam("endHour") String endHour,
			  @FormParam("endMinute") String endMinute) throws JSONException {
		  
			JSONObject jsonObject = new JSONObject();
			
			try{
				//Must be removed:
				jsonObject.put("Operation", "Get Tasks");
				
				FlatMateDAO fmDAO = new FlatMateDAO();
				UserDAO userDAO = new UserDAO();
				//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
				
				TaskDAO taskDAO = new TaskDAO();

				Calendar calendar = new GregorianCalendar(Integer.parseInt(beginYear), Integer.parseInt(beginMonth)-1, Integer.parseInt(beginDay));
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(beginHour));
				calendar.set(Calendar.MINUTE, Integer.parseInt(beginMinute));
				java.util.Date beginDate = calendar.getTime();
				
				calendar = new GregorianCalendar(Integer.parseInt(beginYear), Integer.parseInt(beginMonth)-1, Integer.parseInt(beginDay));
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(beginHour));
				calendar.set(Calendar.MINUTE, Integer.parseInt(beginMinute));
				java.util.Date endDate = calendar.getTime();
				
				List<Task> tasks = taskDAO.getNotNotifiedTasks(beginDate, endDate);
				
				JSONArray jsonArray = new JSONArray();
				
				for (Task task : tasks) {
					
					JSONObject jsonTask = new JSONObject();
					jsonTask.put("id", task.getTaskID());
					jsonTask.put("author", task.getUserID());
					jsonTask.put("flat_id", task.getFlatID());
					jsonTask.put("description", task.getDescription());
					jsonTask.put("type", task.getType());
					
					Date date = task.getTime();
					Calendar cal = new GregorianCalendar();
					cal.setTime(date);
					
					jsonTask.put("year", cal.get(Calendar.YEAR));
					jsonTask.put("month", cal.get(Calendar.MONTH));
					jsonTask.put("day", cal.get(Calendar.DAY_OF_MONTH));
					jsonTask.put("hour", cal.get(Calendar.HOUR_OF_DAY));
					jsonTask.put("minute", cal.get(Calendar.MINUTE));
									
					jsonArray.put(jsonTask);
				}
				
				//Successful operation. 
				jsonObject.put("tasks", jsonArray);
				jsonObject.put("success", true);
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
	  
	  
	  @Path("/create")
	  @POST	
	  @Produces("application/json")
	  public Response createTask(@HeaderParam("Auth") String token,
			  @FormParam("userid") String idUser,
			  @FormParam("flatid") String idFlat,
			  @FormParam("description") String desc,
			  @FormParam("type") String type,
			  @FormParam("year") String year,
			  @FormParam("month") String month,
			  @FormParam("day") String day,
			  @FormParam("hour") String hour,
			  @FormParam("minute") String minute,
			  @FormParam("duration") String duration) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Task");
			
			FlatMateDAO fmDAO = new FlatMateDAO();
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			TaskDAO taskDAO = new TaskDAO();

			Task task = new Task();
			task.setUserID(Integer.valueOf(idUser));
			task.setFlatID(Integer.valueOf(idFlat));
			task.setType(Integer.valueOf(type));
			task.setDescription(desc);
			task.setDuration(Integer.valueOf(duration));
			
			Calendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
			calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
			java.util.Date date = calendar.getTime();
			
			task.setTime(date);
			
			int idTask = taskDAO.addTask(task);
			
			if (idTask > 0) {		
				NotificationDAO notiDAO = new NotificationDAO();
				int notId = notiDAO.createGenericNotification(Integer.valueOf(idUser));
				notiDAO.createTaskNotification(notId, Integer.valueOf(idTask));
				
				List<Integer> destinators = fmDAO.getFlatMembers(Integer.valueOf(idFlat));
				notiDAO.createFlatUsersNotification(destinators, notId);
				
				//Successful operation. 
				jsonObject.put("success", true);
				jsonObject.put("idTask", idTask);				
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
	
	  @Path("/edit")
	  @POST	
	  @Produces("application/json")
	  public Response editTask(@HeaderParam("Auth") String token,
			  @FormParam("userid") String idUser,
			  @FormParam("taskid") String taskid,
			  @FormParam("type") String type,
			  @FormParam("description") String desc,
			  @FormParam("year") String year,
			  @FormParam("month") String month,
			  @FormParam("day") String day,
			  @FormParam("hour") String hour,
			  @FormParam("minute") String minute,
			  @FormParam("duration") String duration) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Edit Task");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 
			
			TaskDAO taskDAO = new TaskDAO();

			Task task = new Task();
			task.setUserID(Integer.valueOf(idUser));
			task.setTaskID(Integer.valueOf(taskid));
			task.setType(Integer.valueOf(type));
			task.setDescription(desc);
			task.setDuration(Integer.valueOf(duration));
			
			Calendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
			calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
			java.util.Date date = calendar.getTime();
			
			task.setTime(date);
			
			taskDAO.editTask(task);
			
			jsonObject.put("success", true);
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
	  
	  @Path("/delete/{task}") 
	  @GET	
	  @Produces("application/json")
	  public Response deleteTask(@HeaderParam("Auth") String token,
			  @PathParam("task") String taskId) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			//Must be removed:
			jsonObject.put("Operation", "Create Shared Object");
			
			UserDAO userDAO = new UserDAO();
			//if (TOKEN_CTRL && userDAO.checkToken(token)) throw new Exception("Token not valid. Please login."); 

			
			TaskDAO taskDAO = new TaskDAO();
			taskDAO.quitTask(Integer.valueOf(taskId));
				
			//Successful operation. 
			jsonObject.put("success", true);				

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
