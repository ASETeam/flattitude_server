package com.flattitude.notification.dto;

import java.sql.Date;

public class UserNotification extends Notification {

	public UserNotification(int notifId, int senderId, Date time) {
		super(notifId, senderId, time);
		
	}

}
