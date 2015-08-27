package com.customized.tools.commands;

import java.io.IOException;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import com.customized.tools.commands.Validator.DBDriverValidator;
import com.customized.tools.commands.Validator.DBURLValidator;
import com.customized.tools.dbtester.DBConnectionTester;
import com.customized.tools.dbtester.DBTester;

@CommandDefinition(name="dbConnectionTest", description = "[-d] ... [-c] ... [-u] ... [-p] ... \nTest Database Connection")
public class DBTesterCommand implements Command<CommandInvocation> {
	
	
	@Option(shortName = 'H', name = "help", hasValue = false,
            description = "display this help and exit")
    private boolean help;
	
	@Option(shortName = 'd',
			name = "driver",
			description = "DB Driver Class",
			required = false,
			defaultValue = {"org.h2.Driver", "com.mysql.jdbc.Driver", "org.postgresql.Driver"},
			validator = DBDriverValidator.class)
	private String driverClass;
	
	@Option(shortName = 'c',
			name = "url",
			description = "DB Connection URL",
			required = false,
			defaultValue = {"jdbc:mysql://localhost:3306/test", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"},
			validator = DBURLValidator.class)
	private String connURL;
	
	@Option(shortName = 'u',
			name = "user",
			description = "DB User",
			required = false,
			defaultValue = {"test_user", "sa"})
	private String user;
	
	@Option(shortName = 'p',
			name = "password",
			description = "DB Password",
			required = false,
			defaultValue = {"test_pass", "sa"})
	private String password;
		

	@Override
	public CommandResult execute(CommandInvocation commandInvocation)throws IOException, InterruptedException {
		
		if(help) {
            commandInvocation.getShell().out().println(commandInvocation.getHelpInfo("dbConnectionTest"));
            return CommandResult.SUCCESS;
        }
		
		DBTester entity = new DBTester();
		entity.setDriver(driverClass);
		entity.setUrl(connURL);
		entity.setUsername(user);
		entity.setPassword(password);
		
		new DBConnectionTester(entity, commandInvocation.getShell().out(), commandInvocation.getShell().err()).execute();
		
		return CommandResult.SUCCESS;
	}


}
