package com.customized.tools.dbtester;

import javax.xml.bind.annotation.XmlElement;

import com.customized.tools.Entity;

public class DBTester extends Entity {

	private static final long serialVersionUID = 4116142538740941723L;

	private String driver;
	
	private String url;
	
	private String username;
	
	private String password;
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlElement(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "driver=" + driver + ", url=" + url + ", username=" + username + ", password=****** ";
	}
}
