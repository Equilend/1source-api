package com.os.contractexport.model;

public class PartyAccess {

	String userName;
	String password;

	public PartyAccess(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

}
