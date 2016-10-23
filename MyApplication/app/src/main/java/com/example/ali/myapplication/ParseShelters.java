package com.example.ali.myapplication;

import android.telephony.SmsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.example.ali.myapplication.SoupKitchenParse.url;

public class ParseShelters extends Thread{

    public static String mac_issues = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

    public static String state;
    public static String city;
    public String sender;
    public static ArrayList<String> shelter_list = new ArrayList<String>();
    public static ArrayList<String> shelter_href = new ArrayList<String>();
    public static ArrayList<String> shelter_contact = new ArrayList<String>();

    public ParseShelters(String city,String state,String sender){
        this.city = city;
        this.state = state;
        this.sender = sender;
        // insert her state abrev
        // does not check for invalid state

        state.toUpperCase();

        // insert here city in the state.
        // does not check for invalid cituy

        city.toLowerCase();
    }

    public void run() {

       ;

        String[] city_arr = city.split("\\s+");

        // mreturns city with '+' for the url
        city = "";

        for (int i = 0 ; i < city_arr.length; ++i) {
            if (i < city_arr.length -1 )
                city += city_arr[i] + "+" ;
            else city += city_arr[i];

        }




        url = "http://www.homelessshelterdirectory.org/cgi-bin/id/city.cgi?city="+ city + "&state=" + state +"";


        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent(mac_issues).get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Elements ele_name = doc.select("div.item_content h4 a[href]");

        int i = 0;
        for (Element e : ele_name) {
            if (i < 19)
                shelter_href.add(e.absUrl("href"));
            ++i;
        }


        Document new_doc = null;
        Element info_ele = null;
        for (String s : shelter_href) {
            try {
                new_doc = Jsoup.connect(s).userAgent(mac_issues).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            info_ele = new_doc.select("div.inside_box").first();
            shelter_contact.add(info_ele.text());
            sendSMS(sender,info_ele.text());

        }

    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


}

