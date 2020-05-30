package com.account;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReturnResponse 
{
	String res;
	int userId;
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}

