package com.customized.tools.jarClassSearcher;

import com.customized.tools.Entity;

public class ClassSearcher extends Entity {

	private static final long serialVersionUID = -966061469552147874L;

	private String className;
	
	private String folderPath;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	@Override
	public String toString() {
		return "ClassSearcher [className=" + className + ", folderPath=" + folderPath + "]";
	}
}
