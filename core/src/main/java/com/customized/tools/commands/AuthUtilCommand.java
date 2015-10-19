package com.customized.tools.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;

import com.customized.tools.ToolsException;
import com.customized.tools.commands.Validator.InputNotNullValidator;

@CommandDefinition(name="auth", description = "[-t]\nA Basic Java Security Authentication Client")
public class AuthUtilCommand implements Command<CommandInvocation> {
    
    public static final String OAUTH2_0_RESULT = 
            "Authentication Result: \n" + 
            "    \"client-id\" = \"{0}\" \n" + 
            "    \"client-secret\" = \"{1}\" \n" +
            "    \"access-token\" =\"{2}\"  \n";
    
    @Option(shortName = 'H', name = "help", hasValue = false,
            description = "display this help and exit")
    private boolean help;
    
    @Option(shortName = 't',
            name = "type",
            description = "Authentication Type",
            required = false,
            defaultValue = {"OAuth-1.0A", "OAuth2-Weibo"},
            validator = InputNotNullValidator.class)
    private String type;

    public CommandResult execute(CommandInvocation commandInvocation) throws IOException, InterruptedException {
        
        if(help) {
            commandInvocation.getShell().out().println(commandInvocation.getHelpInfo("auth"));
            return CommandResult.SUCCESS;
        }
        
        Scanner in = new Scanner(commandInvocation.getShell().in().getStdIn());
        
        switch(type) {
        case "OAuth-1.0A" :
            oauth10Flow(commandInvocation, commandInvocation.getShell().out());
            break;
        case "OAuth2-Weibo":
            oauth20Flow(commandInvocation, commandInvocation.getShell().out());
            break;
        default: 
            commandInvocation.getShell().out().println(type + " not support");
        }
        
        in.close();
        
        return CommandResult.SUCCESS;
    }

    private void oauth10Flow(CommandInvocation commandInvocation, PrintStream out) {
        
    }

    private void oauth20Flow(CommandInvocation commandInvocation, PrintStream out) {
        
        out.println("=== OAuth 2.0 Weibo Workflow ===");
        out.println();
        
        try {
            String clientId = getInput(commandInvocation, "Enter the Client ID = ");
            String clientSecret = getInput(commandInvocation, "Enter the Client Secret = "); 
            
            String redirectUri = getInput(commandInvocation, "Enter callback = ", true);
            
            out.println("Open your broswer, access below URL to execute authorization:");
            out.println("https://api.weibo.com/oauth2/authorize?client_id=" + clientId + "&response_type=code&redirect_uri=" + redirectUri + "&forcelogin=true");
            out.println("");
            
            String authCode = getInput(commandInvocation, "Enter Token Secret (Auth Code, Pin) from previous step = ");
                        
            String url="https://api.weibo.com/oauth2/access_token";
            String parameters = "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=authorization_code" + "&redirect_uri=" + redirectUri + "&code=" + authCode;
            
            commandInvocation.getShell().out().println("Post URL: " + url + "?" + parameters);
            trustAllHttpsCertificates();
            URLConnection conn = new URL(url).openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream());
            outWriter.write(parameters);
            outWriter.flush();
            outWriter.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            commandInvocation.getShell().out().println("Response:");
            while ((line = reader.readLine()) != null){
                commandInvocation.getShell().out().println(line);
            }            
            
            String token = getInput(commandInvocation, "Enter the access token from above json response = ");
            
            out.println("");
            out.println(MessageFormat.format(OAUTH2_0_RESULT, clientId, clientSecret, token));
            
        } catch (Exception e) {
            throw new ToolsException("CST-AUTH", "OAuth 2.0 Authentication failed", e);
        }
        
        
        
    }
    
    private void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = new X509TrustManager(){

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
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
