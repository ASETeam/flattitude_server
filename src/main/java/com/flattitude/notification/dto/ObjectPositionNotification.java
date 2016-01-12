package com.flattitude.notification.dto;

import java.sql.Date;

public class ObjectPositionNotification extends Notification {
	private int objectId;
	
	public ObjectPositionNotification(int notifId, int senderId, Date time, int objectId) {
		super(notifId, senderId, time);	
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

}
