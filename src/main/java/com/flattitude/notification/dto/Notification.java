package com.flattitude.notification.dto;

import java.sql.Date;

public abstract class Notification {
	private int notifId;
	private int senderId;
	private Date time;
	
	public Notification (int notifId, int senderId, Date time) {
		this.setNotifId(notifId);
		this.setSenderId(senderId);
		this.setTime(time);
	}

	public int getNotifId() {
		return notifId;
	}

	public void setNotifId(int notifId) {
		this.notifId = notifId;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
