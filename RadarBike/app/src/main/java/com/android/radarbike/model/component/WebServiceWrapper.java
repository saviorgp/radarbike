package com.android.radarbike.model.component;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by AlexGP on 25/03/2015.
 */
public class WebServiceWrapper {

    private static String SERVER = "http://127.0.0.1:8080/";

    public static void callSetPosition(String ID, String lat, String log){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("ID", ID);
            json.put("lat", lat);
            json.put("long", log);

            HttpPost post = new HttpPost(SERVER + "/api/v1/savePosition");
            post.addHeader(HTTP.CONTENT_TYPE, "application/json");

            System.out.println(json);

            StringEntity se = new StringEntity(json.toString());

            post.setEntity(se);
            response = client.execute(post);

            String output = "";
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    output += output;
                    System.out.println(output);
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject callGetPositions(){
        JSONObject result  = new JSONObject();
        try {

            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            HttpGet get = new HttpGet(SERVER + "/api/v1/getPositions");
            get.addHeader(HTTP.CONTENT_TYPE, "application/json");

            response = client.execute(get);

            String output = "";
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    output += output;
                    System.out.println(output);
                }
            }

            result = new JSONObject(output);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}