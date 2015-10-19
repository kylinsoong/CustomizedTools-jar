package com.customized.tools;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenGrant;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrant;

public class CXFWeiboGetToken {

    public static void main(String[] args) throws URISyntaxException {
        
        String clientID = "3876415151";
        String clientSecret = "5e4f21ac2bd6596689fe16d8215a24e5";
        org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils.Consumer consumer = new org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils.Consumer(clientID, clientSecret);
        
        String authCode = "b8f85a16b4de0106d8dc93cfbd932512";
        
        String accessTokenURL = "https://api.weibo.com/oauth2/access_token";
        
        String callback = "https://api.weibo.com/oauth2/default.html";
        
        WebClient client = WebClient.create(accessTokenURL);
        
        AccessTokenGrant grant = new AuthorizationCodeGrant(authCode, new URI(callback));
        ClientAccessToken clientToken = org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils.getAccessToken(client, consumer, grant, null, false);
        System.out.println("Refresh Token=" + clientToken.getRefreshToken());

        
        

    }

}
