package com.flattitude.dto;

import java.util.Date;

public class SharedObject {
	private int objectID;
	private int flatID;
	private int userID;
    private Date time;
    private String name;
    private String description;
    private float latitude;
    private float longitude;
    
    public SharedObject() {
    	this.objectID = -1;
    	this.latitude = -1;
    	this.longitude = -1;
    	this.name = null;
    	this.description = null;
    }
    
    public SharedObject (int flatID, int userID, Date time, String name, String description, float locX, float locY) {
    	this.setFlatID(flatID);
    	this.setUserID(userID);
    	this.setTime(time);
    	this.setName(name);
    	this.setDescription(description);
    	this.setLatitude(locX);
    	this.setLongitude(locY);
    }
    
	public int getID() {
		return objectID;
	}

	public void setID(int objectID) {
		this.objectID = objectID;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}