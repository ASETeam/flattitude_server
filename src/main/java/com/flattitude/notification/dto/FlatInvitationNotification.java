package com.flattitude.notification.dto;

import java.sql.Date;

public class FlatInvitationNotification extends Notification {
	private int flatId;
	
	public FlatInvitationNotification(int notifId, int senderId, Date time, int flatId) {
		super(notifId, senderId, time);
		this.setFlatId(flatId);
	}

	public int getFlatId() {
		return flatId;
	}

	public void setFlatId(int flatId) {
		this.flatId = flatId;
	}
}
