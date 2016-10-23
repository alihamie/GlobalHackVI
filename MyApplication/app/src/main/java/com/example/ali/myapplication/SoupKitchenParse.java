package com.example.ali.myapplication;

import android.telephony.SmsManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SoupKitchenParse extends Thread{

    public static String mac_issues = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
    public static String sender = "";
    public static String state;
    public static String city;
    public static String url;
    public static ArrayList<String> shelter_list = new ArrayList<String>();
    public static ArrayList<String> shelter_href = new ArrayList<String>();
    public static ArrayList<String> shelter_contact = new ArrayList<String>();


    public SoupKitchenParse(String sender,String city , String state){
        this.sender = sender;
        this.state = state;
        this.city = city;

        Log.e("SOUP" , "CITY " + city + "State " + state);

        // insert her state abrev
        // does not check for invalid state

        state.toUpperCase();

        // insert here city in the state.
        // does not check for invalid cituy

        city.toLowerCase();

    }

    public void run(){
        // TODO Auto-generated method stub


        String[] city_arr = city.split("\\s+");

        // mreturns city with '+' for the url
        city = "";

        for (int i = 0 ; i < city_arr.length; ++i) {
            if (i < city_arr.length -1 )
                city += city_arr[i] + "+" ;
            else city += city_arr[i];

        }

        url = "http://www.homelessshelterdirectory.org/cgi-bin/id/cityfoodbanks.cgi?city="+ city + "&state=" + state +"";


        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent(mac_issues).get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // gets the names of the places
        Elements ele = doc.select("h4");
        int i = 0;
        for (Element link : ele) {
            if (i == ele.size() - 19)
                break;

            shelter_list.add(link.text());
            ++i;
        }


        // gets the href elemnts inorder to further parse the address
        Elements div_ele = doc.select("div.item_content h4 a[href]");
        i = 0;
        for (Element e: div_ele) {
            if(i == shelter_list.size() )
                break;

            shelter_href.add(e.absUrl("href"));
            ++i;
        }

        // gets the elements from the links within the link #mindBlown
        Document new_doc = null;
        Element info_ele = null;
        int count = 0;
        for (String s : shelter_href) {
            try {
                new_doc = Jsoup.connect(s).userAgent(mac_issues).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            info_ele = new_doc.select("div.inside_box").first();
            if(info_ele != null)
            shelter_contact.add(info_ele.text());
            if(count < 5) {
                sendSMS(sender, info_ele.text());
                count++;
            }else{
                break;
            }



        }



    }


    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

}
