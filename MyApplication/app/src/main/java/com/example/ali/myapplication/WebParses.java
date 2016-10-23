package com.example.ali.myapplication;

import android.telephony.SmsManager;
import android.util.Log;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Ali on 10/22/2016.
 */

public class WebParses extends Thread  {


    private String sender;
    public WebParses(String sender){


        this.sender = sender;
    }




    public void run(){

        try {
            Document doc = Jsoup.connect("http://www.rentassistance.us/ci/fl-tallahassee").get();

            Elements div = doc.select("div.listing");

            for(int i = 0; i < div.size(); ++i){

                sendSMS(sender,div.get(i).text().toString().split("\\.")[1] + "\n"
                + div.get(i).select("div.listing_contact").text().toString().split(",")[0]
                + "("+div.get(i).select("div.listing_contact").text().toString().split("\\(")[1]);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


}
