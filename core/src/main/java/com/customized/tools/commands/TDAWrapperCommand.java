package com.customized.tools.commands;

import java.io.IOException;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import com.customized.tools.commands.Validator.FileValidator;
import com.customized.tools.model.TDAEntity;

@CommandDefinition(name="tda", description = "Java TDA Analyzer")
public class TDAWrapperCommand implements Command<CommandInvocation> {
	
	@Option(shortName = 'H', name = "help", hasValue = false,
            description = "display this help and exit")
    private boolean help;
	
	@Option(shortName = 'p',
			name = "path", 
			description = "TDA file path", 
			defaultValue = {"tdump.out"},
			validator = FileValidator.class)
	private String path;
	

	@Override
	public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {
		
		if(help) {
            commandInvocation.getShell().out().println(commandInvocation.getHelpInfo("tda"));
            return CommandResult.SUCCESS;
        }
		
		TDAEntity entity = new TDAEntity();
		entity.setPath(path);
		
//		tda.setEntity(entity);
//		
//		tda.execute();
		
		return CommandResult.SUCCESS;
	}

}
