package com.android.radarbike.model.component;

import com.android.radarbike.utils.Logger;

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

    private static String SERVER = "http://radarbike-izze.rhcloud.com/";

    public static void callSetPosition(String imei, double lat, double lng){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("imei", imei);
            json.put("lat", lat);
            json.put("lng", lng);

            HttpPost post = new HttpPost(SERVER + "position");
            post.addHeader(HTTP.CONTENT_TYPE, "application/json");

            System.out.println(json);

            StringEntity se = new StringEntity(json.toString());

            post.setEntity(se);
            response = client.execute(post);

            StringBuffer output = new StringBuffer();
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("Output from Server .... \n");
                String line = "";
                while ((line = br.readLine()) != null) {
                    output.append(line);
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

            HttpGet get = new HttpGet(SERVER + "lastPosition");
            get.addHeader(HTTP.CONTENT_TYPE, "application/json");

            response = client.execute(get);

            StringBuffer output = new StringBuffer();
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("Output from Server .... \n");
                String line = "";
                while ((line = br.readLine()) != null) {
                    output.append(line);
                    System.out.println(output);
                }
            }

            result = new JSONObject(output.toString());

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void callCheckout(String imei){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;

            JSONObject json  = new JSONObject();

            json.put("imei", imei);

            HttpPost post = new HttpPost(SERVER + "checkout");
            post.addHeader(HTTP.CONTENT_TYPE, "application/json");

            System.out.println(json);

            StringEntity se = new StringEntity(json.toString());

            post.setEntity(se);
            response = client.execute(post);

            StringBuffer output = new StringBuffer();
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("Output from Server .... \n");
                String line = "";
                while ((line = br.readLine()) != null) {
                    output.append(line);
                    System.out.println(output);
                }
            }

        } catch(Exception e) {
            Logger.LOGD(e);
            e.printStackTrace();
        }
    }
}