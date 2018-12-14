package com.orange.domain;

import org.hibernate.validator.constraints.NotEmpty;

public class Authenticate {

	private String userId;
	@NotEmpty(message="비밀번호를 입력하세요")
	private String password;
	private int wiid;
	private String actionId;
	private String oldStateId;
	
	public Authenticate() {
	}

	public Authenticate(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getWiid() {
		return wiid;
	}

	public void setWiid(int wiid) {
		this.wiid = wiid;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getOldStateId() {
		return oldStateId;
	}

	public void setOldStateId(String oldStateId) {
		this.oldStateId = oldStateId;
	}

	public boolean matchPassword(String password) {
		return this.password.equals(password);
	}

	@Override
	public String toString() {
		return "Authenticate [userId=" + userId + ", password=" + password + ", wiid=" + wiid + ", actionId=" + actionId
				+ ", oldStateId=" + oldStateId + "]";
	}
	
}
