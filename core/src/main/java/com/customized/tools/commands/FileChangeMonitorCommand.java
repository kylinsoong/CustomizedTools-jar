package com.customized.tools.commands;

import java.io.IOException;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import com.customized.tools.commands.Validator.DirectoryValidator;
import com.customized.tools.commands.Validator.InputNotNullValidator;
import com.customized.tools.filechangemonitor.FileChangeMonitor;
import com.customized.tools.filechangemonitor.Monitor;

@CommandDefinition(name="monitor", description = "[-p] ... [-n] ... \nMonitor a file system")
public class FileChangeMonitorCommand implements Command<CommandInvocation> {
	
	@Option(shortName = 'H', name = "help", hasValue = false,
            description = "display this help and exit")
    private boolean help;
	
	@Option(shortName = 'p',
			name = "folderPath", 
			description = "The FileChangeMonitor file system path", 
			defaultValue = {"/home/kylin/server/jboss-eap-6.3"},
			validator = DirectoryValidator.class)
	private String folderPath;
	
	@Option(shortName = 'n',
			name = "resultFile", 
			description = "The FileChangeMonitor result file name",
			defaultValue = {"monitor.out"},
			validator = InputNotNullValidator.class)
	private String resultFile;
	

	@Override
	public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {
		
		if(help) {
            commandInvocation.getShell().out().println(commandInvocation.getHelpInfo("monitor"));
            return CommandResult.SUCCESS;
        }
		
		Monitor entity = new Monitor();
		entity.setFolderPath(folderPath);
		entity.setResultFile(resultFile);
		
		new FileChangeMonitor(entity, commandInvocation.getShell().out(), commandInvocation.getShell().err()).execute();
		
		
        return CommandResult.SUCCESS;
	}
	
}
