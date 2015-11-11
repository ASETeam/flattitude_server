package com.flattitude.dto;

import java.util.Date;

public class User {

	private int id = 0; // This is an special case for this model, since
								// we will have only one user
	private String email;
	private String firstname;
	private String lastname;
	private String phonenbr;
	private Date birthdate;
	private boolean loggedin;

	public User(String email, String firstname, String lastname) {
		this.setEmail(new String(email));
		this.setFirstname(new String(firstname));
		this.setLastname(new String(lastname));
		this.setPhonenbr("");
		this.setBirthdate(null);
		this.setLoggedin(false);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhonenbr() {
		return phonenbr;
	}

	public void setPhonenbr(String phonenbr) {
		this.phonenbr = phonenbr;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public boolean isLoggedin() {
		return loggedin;
	}

	public void setLoggedin(boolean loggedin) {
		this.loggedin = loggedin;
	}
}