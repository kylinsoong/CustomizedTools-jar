package com.customized.tools.commands;

import java.io.IOException;
import java.io.PrintStream;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.SinaWeiboApi20;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.customized.tools.ToolsException;
import com.customized.tools.commands.Validator.InputNotNullValidator;

@CommandDefinition(name="auth", description = "[-t]\nA Basic Java Security Authentication Client")
public class AuthUtilCommand implements Command<CommandInvocation> {
    
    @Option(shortName = 'H', name = "help", hasValue = false,
            description = "display this help and exit")
    private boolean help;
    
    @Option(shortName = 't',
            name = "type",
            description = "Authentication Type",
            required = false,
            defaultValue = {"OAuth2-Weibo"},
            validator = InputNotNullValidator.class)
    private String type;

    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {
        
        if(help) {
            commandInvocation.getShell().out().println(commandInvocation.getHelpInfo("auth"));
            return CommandResult.SUCCESS;
        }
                
        switch(type) {
        case "OAuth-1.0A" :
            oauth10Flow(commandInvocation, commandInvocation.getShell().out());
            break;
        case "OAuth2-Weibo":
            oauth2WeiboFlow(commandInvocation, commandInvocation.getShell().out());
            break;
        default: 
            commandInvocation.getShell().out().println(type + " not support");
        }
                
        return CommandResult.SUCCESS;
    }

    private void oauth10Flow(CommandInvocation commandInvocation, PrintStream out) {
        
    }


    private void oauth2WeiboFlow(CommandInvocation commandInvocation, PrintStream out) {
        
        out.println("=== OAuth 2.0 Weibo Workflow ===");
        out.println();
        
        try {
			String clientId = getInput(commandInvocation, "Enter the Client ID = ");
			String clientSecret = getInput(commandInvocation, "Enter the Client Secret = "); 
			String redirectUri = getInput(commandInvocation, "Enter callback = ", true);
			
			OAuthService service = new ServiceBuilder()
					.provider(SinaWeiboApi20.class)
					.apiKey(clientId)
					.apiSecret(clientSecret)
					.callback(redirectUri)
					.build();
			
			out.println("Open your broswer, access below URL to execute authorization:");
            out.println(service.getAuthorizationUrl(null));
            out.println();
            String authCode = getInput(commandInvocation, "Enter Authorization Code from previous step = ");
            Verifier verifier = new Verifier(authCode);
            out.println(service.getAccessToken(null, verifier));
            
		} catch (Exception e) {
			throw new ToolsException("CST-AUTH", "Weibo OAuth 2.0 remove token failed", e);
		}
      
    }
    
    private String getInput(CommandInvocation commandInvocation, String message) throws Exception {
        return getInput(commandInvocation, message, false); 
    }    
    
    private String getInput(CommandInvocation commandInvocation, String message, boolean allowNull) throws Exception {
        while (true) {
            commandInvocation.getShell().out().print(message);
            String input = commandInvocation.getInputLine();
            input = input.trim();
            if (input.length() > 1) {
                System.out.println("");
                return input;
            }
            
            if (allowNull) {
                return null;
            }
        }
    }

}
