package com.customized.tools.gcviewer;

import java.io.PrintStream;

import com.customized.tools.AbstractTools;
import com.customized.tools.gcviewer.GCViewerException;
import com.tagtraum.perf.gcviewer.GCViewer;

public class GCViewerWrapper extends AbstractTools {
    
    private GCViewerEntity gcViwer;
			
	private PrintStream out;
    
    private PrintStream err;

	public GCViewerWrapper(GCViewerEntity gcViwer, PrintStream out, PrintStream err) {
		this.gcViwer = gcViwer;
		this.out = out;
		this.err = err;
	}

		
	public void execute() {
		
		out.println("GCViewWrapper start GCViewer");
			
		try {
		    String[] args = new String[]{gcViwer.getPath(), gcViwer.getName()};
		    
		    GCViewer.main(args);
		} catch (Throwable e) {
			GCViewerException ex = new GCViewerException("", e);
			err.println("GCViewer Return a Error, " + ex.getMessage());
		}
	}

}
