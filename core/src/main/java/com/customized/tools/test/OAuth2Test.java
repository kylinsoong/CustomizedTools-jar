package com.customized.tools.test;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;

public class OAuth2Test {

    public static void main(String[] args) throws IOException {

        String get_token_info = "https://api.weibo.com/oauth2/get_token_info";
        WebClient wc = WebClient.create(get_token_info);
        Response resp = wc.form(new Form().param("access_token", "2.00PZtDyBBfC2OE0d5037de290sImQE"));
        if(resp.getStatus() == 200) {
            IOUtils.copy((InputStream) resp.getEntity(), System.out);
        }
    }

}
