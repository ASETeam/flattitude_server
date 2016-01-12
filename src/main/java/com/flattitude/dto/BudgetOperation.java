package com.flattitude.dto;

import java.util.Date;

public class BudgetOperation {
	private int id;
	private int flatid;
	private int userid;
	private float amount;
	private Date date;
	private String description;
	
	public BudgetOperation (int id, int flatid, int userid, float amount, Date date, String description) {
		this.id = id;
		this.flatid = flatid;
		this.userid = userid;
		this.amount = amount;
		this.date = date;
		this.description = description;
	}

	public int getFlatid() {
		return flatid;
	}

	public void setFlatid(int flatid) {
		this.flatid = flatid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
