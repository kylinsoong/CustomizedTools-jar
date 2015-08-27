package com.customized.tools.searcher;

import com.customized.tools.Entity;

public class Searcher extends Entity {

	private static final long serialVersionUID = -3368623835034787675L;

	private String fileName;
	
	private String folderPath;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	@Override
	public String toString() {
		return "Searcher [fileName=" + fileName + ", folderPath=" + folderPath + "]";
	}
}
