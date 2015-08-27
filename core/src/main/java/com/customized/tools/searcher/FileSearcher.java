package com.customized.tools.searcher;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import com.customized.tools.AbstractTools;

public class FileSearcher extends AbstractTools {
		
	private Searcher fileSearcher ;
	
	private PrintStream out;
    
    private PrintStream err;
    
    public FileSearcher(Searcher fileSearcher, PrintStream out, PrintStream err) {
        this.fileSearcher = fileSearcher ;
        this.out = out ;
        this.err = err ;
    }
	

	public void execute() {
		

		try {
			String searchName = fileSearcher.getFileName();
			String searchFolder = fileSearcher.getFolderPath();
			
			File parentfile = new File(searchFolder);
			
			if(!parentfile.exists() || !parentfile.isDirectory() || searchName.length() == 0) {
			    err.println(new FileSearcherException("Search folder not exist.").getMessage());
				return;
			}
			
			if( searchName.length() == 0 || searchName.trim().length() == 0) {
			    err.println(new FileSearcherException("Error search file name format").getMessage());
				return;
			}
			
			out.println("FileSearcher start, searching file '" + searchName + "' under " + searchFolder + LN);
			
			List<String> result = new SearcherImpl(searchName, searchFolder, out, err).search();
			
			String propmtStr = "File '" + searchName + "' be found " + result.size() + " times:";
			out.println(propmtStr);
			
			for(int i = 0 ; i < result.size() ; i ++) {
			    out.println(TAB2 + result.get(i));
			}
			
			//TODO-- add dump propmtStr, result to file
		} catch (Throwable e) {
			FileSearcherException ex = new FileSearcherException("File Searcher return a Exception" ,e);
			err.println(ex.getMessage());
		}
	}

}
