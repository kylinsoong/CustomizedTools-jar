package com.customized.tools;

import java.io.Serializable;

public class Entity implements Serializable {

	private static final long serialVersionUID = -1479510289888593457L;
	
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
