package com.customized.tools.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandOperation;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.terminal.Key;

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
            defaultValue = {"OAuth-1.0A", "OAuth2-Weibo"},
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

    /**
     * 请求授权    oauth2/authorize    请求用户授权Token
     * 获取授权    oauth2/access_token     获取授权过的Access Token
     * 授权查询    oauth2/get_token_info   查询用户access_token的授权相关信息
     * 替换授权    oauth2/get_oauth2_token     OAuth1.0的Access Token更换至OAuth2.0的Access Token
     * 授权回收    OAuth2/revokeoauth2     授权回收接口，帮助开发者主动取消用户的授权 
     * @param commandInvocation
     * @param out
     */
    private void oauth2WeiboFlow(CommandInvocation commandInvocation, PrintStream out) {
        
        out.println("=== OAuth 2.0 Weibo Workflow ===");
        out.println();
        
        while(true) {
            out.println("Select a, b or c to start");
            out.println("    a. get authorized access token");
            out.println("    b. get access token information");
            out.println("    c. remove authorized access token");
            
            CommandOperation operation = null;
            try {
                operation = commandInvocation.getInput();
            }
            catch (InterruptedException e) {
                out.println(e.getMessage());
            }
            Key key = operation.getInputKey();
            
            if(key.equals(Key.a)){
                weibo_access_token(commandInvocation, out);
                break;
            } else if(key.equals(Key.b)){
                weibo_query_access_token(commandInvocation, out);
                break;
            } else if(key.equals(Key.c)) {
                weibo_remove_access_token(commandInvocation, out);
                break;
            } 
        }
    }
    
    private void weibo_remove_access_token(CommandInvocation commandInvocation, PrintStream out) {
        
        try {
            out.println("");
            String access_token = getInput(commandInvocation, "Enter Access Token = ");
            String remove = "https://api.weibo.com/oauth2/revokeoauth2";
            Bus bus = BusFactory.getThreadDefaultBus();
            WebClient wc = createWebClient(remove, bus);
            Response resp = wc.form(new Form().param("access_token", access_token));
            out.println("");
            out.println("Remove Access Token:");
            IOUtils.copy((InputStream) resp.getEntity(), out);
            wc.close();
        } catch (Exception e) {
            throw new ToolsException("CST-AUTH", "Weibo OAuth 2.0 remove token failed", e);
        }
        
        
    }

    private void weibo_query_access_token(CommandInvocation commandInvocation, PrintStream out) {

        try {
            out.println("");
            String access_token = getInput(commandInvocation, "Enter Access Token = ");
            String get_token_info = "https://api.weibo.com/oauth2/get_token_info";
//            Bus bus = BusFactory.getThreadDefaultBus();
            WebClient wc = WebClient.create(get_token_info);/*createWebClient(get_token_info, bus);*/
            Response resp = wc.form(new Form().param("access_token", access_token));
            out.println("");
            out.println("Access Token Info:");
            IOUtils.copy((InputStream) resp.getEntity(), out);
            wc.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ToolsException("CST-AUTH", "Weibo OAuth 2.0 get token info failed", e);
        }
    }

    private void weibo_access_token(CommandInvocation commandInvocation, PrintStream out) {

        try {
            out.println("");
            String clientId = getInput(commandInvocation, "Enter the Client ID = ");
            String clientSecret = getInput(commandInvocation, "Enter the Client Secret = "); 
            
            String redirectUri = getInput(commandInvocation, "Enter callback = ", true);
            
            out.println("Open your broswer, access below URL to execute authorization:");
            out.println("https://api.weibo.com/oauth2/authorize?client_id=" + clientId + "&response_type=code&redirect_uri=" + redirectUri + "&forcelogin=true");
            out.println("");
            
            String authCode = getInput(commandInvocation, "Enter Auth Code from previous step = ");
                        
            String access_token = "https://api.weibo.com/oauth2/access_token";
            
            Bus bus = BusFactory.getThreadDefaultBus();
            WebClient wc = createWebClient(access_token, bus);
            
            Response resp = wc.form(new Form().param("client_id", clientId).param("client_secret", clientSecret).param("grant_type", "authorization_code").param("code", authCode).param("redirect_uri", redirectUri));
            
            out.println("");
            out.println("Access Token:");
            IOUtils.copy((InputStream) resp.getEntity(), out);
            wc.close();
        } catch (Exception e) {
            throw new ToolsException("CST-AUTH", "Weibo OAuth 2.0 access token failed", e);
        }
    }
    
    private WebClient createWebClient(String baseAddress, Bus bus) {
        
        JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setBus(bus);
        bean.setAddress(baseAddress);
        return bean.createWebClient();
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
