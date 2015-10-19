package com.customized.tools;

import java.net.URI;
import java.util.Scanner;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenGrant;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrant;

public class CXFWeiboClient {
    
    static String clientId = "3876415151";
    
    static String clientSecret = "5e4f21ac2bd6596689fe16d8215a24e5";
    
    static String redirectUri="https://api.weibo.com/oauth2/default.html";

    public static void main(String[] args) throws Exception {
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("=== OAuth 2.0 Workflow ===");
        System.out.println();

        String clientID = "3876415151";
        String clientSecret = "5e4f21ac2bd6596689fe16d8215a24e5";
        org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils.Consumer consumer = new org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils.Consumer(clientID, clientSecret);
        
//        String authorizeURL = getInput(in, "Enter the User Authorization URL = ");
        
        String callback = "https://api.weibo.com/oauth2/default.html";

        
        String authenticateURL = "https://api.weibo.com/oauth2/authorize?client_id=" + clientId + "&response_type=code&redirect_uri=" + redirectUri + "&forcelogin=true";
        
        System.out.println("Cut & Paste the URL in a web browser, and Authticate");
        System.out.println("Authorize URL  = " + authenticateURL);
        System.out.println("");
        
        String authCode = getInput(in, "Enter Token Secret (Auth Code, Pin) from previous step = ");
        
        String accessTokenURL = getInput(in, "Enter the Access Token URL = ");
        WebClient client = WebClient.create(accessTokenURL);
        
        AccessTokenGrant grant = new AuthorizationCodeGrant(authCode, new URI(callback));
        ClientAccessToken clientToken = org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils.getAccessToken(client, consumer, grant, null, false);
        System.out.println("Refresh Token=" + clientToken.getRefreshToken());

        
    }
    
    public static String getInput(Scanner in, String message) throws Exception {
        return getInput(in, message, false); 
    }    
    
    public static String getInput(Scanner in, String message, boolean allowNull) throws Exception {
        while (true) {
            System.out.print(message);
            String input = in.nextLine();
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
