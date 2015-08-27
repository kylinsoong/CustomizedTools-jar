package com.customized.tools.gcviewer;

import com.customized.tools.Entity;

public class GCViewerEntity extends Entity {

	private static final long serialVersionUID = -4055181623049821767L;

	private String path;
	
	private String name;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "GCViewerEntity [path=" + path + ", name=" + name + "]";
	}

}
