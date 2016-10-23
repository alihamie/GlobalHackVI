package com.example.ali.myapplication;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


/**
 * Created by Ali on 10/22/2016.
 */

public class GeoCoding  {
    public final static String KEY = "AIzaSyD7gSP25l-ErX4YuUxbeNJxxIhbMFtcgig";

    Context c;
    public static String result = "";

    private static resultCallBack mlistener;

    public GeoCoding(Context c ) throws IOException {

        this.c = c;
        mlistener = (resultCallBack) c;
    }

    public interface resultCallBack{

        public String getFinalResult(String result,int requestCode,String sender);
    }



    public static void getResult(Context c, final int code, final String sender,String zip) {

        mlistener = (resultCallBack) c;
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+zip+"&key=" + KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                                JSONObject j = new JSONObject(response);
                                JSONArray r = j.getJSONArray("results");
                                JSONObject addresses = r.getJSONObject(0);
                                JSONArray address = addresses.getJSONArray("address_components");
                                String city ="miami";
                                String state="fl";
                                String stateFullName="Florida";
                            for(int i = 0; i < address.length(); ++i){
                                    JSONArray type = address.getJSONObject(i).getJSONArray("types");
                                    if(type.get(0).equals("administrative_area_level_1")){
                                        Log.e("Ali",response);
                                        state = address.getJSONObject(i).getString("short_name");
                                        stateFullName = address.getJSONObject(i).getString("long_name");
                                    }

                                    if(type.get(0).equals("locality")){
                                        city = address.getJSONObject(i).getString("long_name");
                                    }
                                }
                                Log.e("Ali","REsponse is " + city + "  state: " + state);
                                result = city + ">" + state + ">" +stateFullName;

                                mlistener.getFinalResult(result,code,sender);

                            }catch(JSONException e){
                                e.printStackTrace();
                            }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        queue.add(stringRequest);
    }
}




