package com.metrictrade.pushserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class RegisterExample {
	
	static HashMap<String, String> params = new HashMap<String, String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		URL url;
        try {
             
            url = new URL("http://192.168.65.24/gcm_server_files/register.jsp");
             
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + "http://192.168.65.24/gcm_server_files/register.jsp");
        }
         
        params.put("regId", "test");
	    params.put("username", "konstantinos_ar");
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
         
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
         
        String body = bodyBuilder.toString();
         
        System.out.println("Posting '" + body + "' to " + url);
         
        byte[] bytes = body.getBytes();
         
        HttpURLConnection conn = null;
        try {
             
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
             
            // handle the response
            int status = conn.getResponseCode();
             
            // If response is not success
            if (status != 200) {
                 
              throw new IOException("Post failed with error code " + status);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

	}

}