package com.os.marktomarket;

import java.io.Serializable;

public class AuthToken implements Serializable {

	private static final long serialVersionUID = -3612839311794332193L;
	
	String access_token;
	Integer expires_in;
	Integer refresh_expires_in;
	String refresh_token;
	String token_type;
	Integer not_before_policy;
	String session_state;
	String scope;

	Long createMillis;
	
	public AuthToken() {
		createMillis = System.currentTimeMillis();
	}
	
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public Integer getRefresh_expires_in() {
		return refresh_expires_in;
	}

	public void setRefresh_expires_in(Integer refresh_expires_in) {
		this.refresh_expires_in = refresh_expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public Integer getNot_before_policy() {
		return not_before_policy;
	}

	public void setNot_before_policy(Integer not_before_policy) {
		this.not_before_policy = not_before_policy;
	}

	public String getSession_state() {
		return session_state;
	}

	public void setSession_state(String session_state) {
		this.session_state = session_state;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Long getCreateMillis() {
		return createMillis;
	}

	public void setCreateMillis(Long createMillis) {
		this.createMillis = createMillis;
	}
}
