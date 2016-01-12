package com.flattitude.dto;

import java.util.Date;

public class Task {
	private int taskID;
	private int flatID;
	private int userID;
    private Date time;
    private String description;
    private int type;
    private int duration;
    
    public Task() {
    	this.setTaskID(-1);    	
    	this.description = null;
    	this.type = -1;
    	this.time = null;
    	this.setDuration(-1);
    }

    public Task (int flatID, int userID, Date time, String description, int type, int duration) {
    	this.setFlatID(flatID);
    	this.setUserID(userID);
    	this.setTime(time);
    	this.setDescription(description);
    	this.setType(type);
    	this.setDuration(duration);
    }
    
	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public int getFlatID() {
		return flatID;
	}

	public void setFlatID(int flatID) {
		this.flatID = flatID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
    

}
