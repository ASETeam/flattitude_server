package com.flattitude.notification.dto;

import java.sql.Date;

public class TaskNotification extends Notification {
	private int taskId;
	
	public TaskNotification(int notifId, int senderId, Date time, int objectId) {
		super(notifId, senderId, time);	
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

}
