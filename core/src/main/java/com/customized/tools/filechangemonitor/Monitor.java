package com.customized.tools.filechangemonitor;

import com.customized.tools.Entity;

public class Monitor extends Entity {

	private static final long serialVersionUID = 4590295501517933906L;

	private String folderPath;
	
	private String resultFile;

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getResultFile() {
		return resultFile;
	}

	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}

	@Override
	public String toString() {
		return "Monitor [folderPath=" + folderPath + ", resultFile=" + resultFile + "]";
	}
}
