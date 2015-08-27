package com.customized.tools.commands;

import java.io.IOException;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import com.customized.tools.commands.Validator.FileValidator;
import com.customized.tools.commands.Validator.InputNotNullValidator;
import com.customized.tools.gcviewer.GCViewerEntity;
import com.customized.tools.gcviewer.GCViewerWrapper;

@CommandDefinition(name="gcViewer", description = "JVM Garbage Collection Log Analyzer")
public class GCViewerWrapperCommand implements Command<CommandInvocation> {
	
	@Option(shortName = 'H', name = "help", hasValue = false,
            description = "display this help and exit")
    private boolean help;
	
	@Option(shortName = 'p',
			name = "filePath", 
			description = "GCViewer gc log file path", 
			defaultValue = {"gc.log"},
			validator = FileValidator.class)
	private String filePath;
	
	@Option(shortName = 'n',
			name = "resultFile", 
			description = "GCViewer result file name",
			defaultValue = {"export.csv"},
			validator = InputNotNullValidator.class)
	private String resultFile;
	
	@Override
	public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {
		
		if(help) {
            commandInvocation.getShell().out().println(commandInvocation.getHelpInfo("gcViewer"));
            return CommandResult.SUCCESS;
        }
		
		GCViewerEntity entity = new GCViewerEntity();
		entity.setPath(filePath);
		entity.setName(resultFile);
				
		new GCViewerWrapper(entity, commandInvocation.getShell().out(), commandInvocation.getShell().err()).execute();
		
		return CommandResult.SUCCESS;
	}

}
