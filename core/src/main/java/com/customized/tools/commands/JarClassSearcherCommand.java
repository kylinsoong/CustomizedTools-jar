package com.customized.tools.commands;

import java.io.IOException;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import com.customized.tools.commands.Validator.DirectoryValidator;
import com.customized.tools.commands.Validator.InputNotNullValidator;
import com.customized.tools.jarClassSearcher.ClassSearcher;
import com.customized.tools.jarClassSearcher.JarClassSearcher;

@CommandDefinition(name="searcher", description = "[-p] ... [-n] ... \nSearch class from jar file")
public class JarClassSearcherCommand implements Command<CommandInvocation>{
	
	@Option(shortName = 'H', name = "help", hasValue = false,
            description = "display this help and exit")
    private boolean help;
	
	@Option(shortName = 'p',
			name = "folderPath", 
			description = "jarClassSearcher folder path", 
			defaultValue = {"/home/kylin/server/jboss-eap-6.3"},
			validator = DirectoryValidator.class)
	private String folderPath;
	
	@Option(shortName = 'n',
			name = "className", 
			description = "jarClassSearcher class name",
			defaultValue = {"org.jboss.modules.Main"},
			validator = InputNotNullValidator.class)
	private String className;
	
	@Override
	public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {
		
		if(help) {
            commandInvocation.getShell().out().println(commandInvocation.getHelpInfo("searcher"));
            return CommandResult.SUCCESS;
        }

		
		ClassSearcher entity = new ClassSearcher();
		entity.setFolderPath(folderPath);
		entity.setClassName(className);
				
		new JarClassSearcher(entity, commandInvocation.getShell().out(), commandInvocation.getShell().err()).execute();
		
		return CommandResult.SUCCESS;
	}

}
