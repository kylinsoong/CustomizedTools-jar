package com.customized.tools.searcher;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class SearcherImpl {
	
	private String searchName ;
	
	private String searchFolder;
	
	private PrintStream out;
	
	private PrintStream err;

	public SearcherImpl(String searchName, String searchFolder, PrintStream out, PrintStream err) {
		this.searchName = searchName;
		this.searchFolder = searchFolder;
		this.out = out;
		this.err = err;
	}

	public List<String> search() throws ZipException, IOException {
		
	    out.println("Searching " + searchName + " under " + searchFolder);
		
		List<String> result = new ArrayList<String>();
		
		search(result, new File(searchFolder), searchName);
		
		return result;
	}

	private void search(List<String> result, File file, String searchName) throws ZipException, IOException {
		
		for(File f : file.listFiles()) {
			
			if(f.getName().contains(searchName)) {
				String path = f.getAbsolutePath() ;
				result.add(path.substring(path.indexOf(searchFolder)));
			}
			
			if(f.isDirectory()) {
				search(result, f, searchName);
			}  else if(f.getName().endsWith(".zip") || f.getName().endsWith(".jar") || f.getName().endsWith(".war") || f.getName().endsWith(".ear") || f.getName().endsWith(".esb")) {
				
				ZipFile zipFile = null;
				
				try {
					zipFile = new ZipFile(f);
					String path = f.getAbsolutePath() ;
					path = path.substring(path.indexOf(searchFolder));
					traverseZipFile(result, path, zipFile, searchName);
				} catch (Exception e) {
					String prompt = "can not create zipFile via " + f ;
					err.println(prompt + ", ignored");
				}
				
			}
		}
	}

	private void traverseZipFile(List<String> result, String prefix, ZipFile zip, String searchName) {
		
		@SuppressWarnings("unchecked")
		Enumeration <ZipEntry>en = (Enumeration<ZipEntry>) zip.entries();
		
		while(en.hasMoreElements()) {
			
			ZipEntry entry = en.nextElement() ;
			
			if(entry.getName().contains(searchName)) {
				result.add(prefix + " $ " + entry.getName());
			}
		}
	}
	
}
