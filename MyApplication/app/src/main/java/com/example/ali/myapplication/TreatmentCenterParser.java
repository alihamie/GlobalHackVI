package com.example.ali.myapplication;

import android.content.Context;
import android.telephony.SmsManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
//import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.regex.*;
import java.net.*;
import java.io.*;
import org.json.*;

public class TreatmentCenterParser implements Runnable {
    //	public Hashtable<String, Integer> treatmentCenterByState= new Hashtable<String,Integer>();
//	Hashtable<String,ArrayList<TreatmentCenters>> tableOfTreatmentCentresByStates = new Hashtable<String,ArrayList<TreatmentCenters>>();
//    public String page="http://www.freetreatmentcenters.com/";
    public ArrayList<TreatmentCenters> listOfCities = new ArrayList<TreatmentCenters>();
    public ArrayList<TreatmentCenters> finalList = new ArrayList<TreatmentCenters>();
    String city_ToSearch="";
    public String currentZip;
    public String currentState;
    public String sender;
    public TreatmentCenterParser(String state, String zip, Context c , String sender){
        city_ToSearch = getResponse(zip);
        currentState = state;
        currentZip = zip;
        this.sender = sender;

    }

    public void parseIndividualStates(String state)
    {
        String state_page ="http://www.freetreatmentcenters.com/state/";

        state= state.toLowerCase();
        switch (state)
        {
            case "westvirginia":
                state= "west_virginia";
                break;
            case "newjersey":
                state= "new_jersey";
                break;
            case "newyork":
                state = "new_york";
                break;
            case "northdakota":
                state= "north_dakota";
                break;
            case "northcarolina":
                state = "north_carolina";
                break;
            case "southdakota":
                state = "south_dakota";
                break;
            case "rhodeisland":
                state = "rhode_island";
                break;
        }


        String final_state_page = state_page+state;
        Document doc ;

        try {
            URL url = new URL(final_state_page);
            try {
                doc= Jsoup.parse(url, 3000);
                //System.out.println(doc.html());
                Elements elements=doc.getElementsByAttributeValueStarting("style", "float:left;");
                for (Element e:elements)
                {
                    TreatmentCenters obj= new TreatmentCenters();
                    obj.city="";
                    obj.zipCode="";
                    if (e.text().contains(","))
                    {
                        String[] arr = e.text().split(",");
                        obj.address = arr[0];
                        obj.zipCode = arr[1].substring(0, 11);
                        if (arr[1].contains("Email"))
                        {
                            arr[1]= arr[1].replace("Email", "");
                        }
                        if (arr[1].contains("Website"))
                        {
                            arr[1]= arr[1].replace("Website", "");
                        }
                        obj.contactnumber = arr[1].substring(12, arr[1].length());

                        listOfCities.add(obj);
                    }

                }
            }catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println(state);
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        printoutTable();
        //getResponse();
    }

    public void printoutTable()
    {
        try
        {
            for (TreatmentCenters t:listOfCities)
            {
                if (getResponse(t.zipCode)!=null)
                {
                    t.city = getResponse(t.zipCode);
                    if (t.city.equals(city_ToSearch))
                    {
                        finalList.add(t);
                    }
                }
                else
                {
                    t.city="";
                }
            }
        }
        catch(Exception e)
        {
            //e.printStackTrace();
        }
        printFinalList();
    }

    public void printFinalList()
    {
        for (TreatmentCenters a:finalList)
        {
            System.out.println(a.address);
            System.out.println(a.zipCode);
            System.out.println(a.contactnumber);
            System.out.println(a.city);
        }
    }

//    public void getResponse()
//    {
//    	 try {
//			URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=32303");
//			 HttpURLConnection connection = null;
//			try {
//				connection = (HttpURLConnection) url.openConnection();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    try {
//				connection.setRequestMethod("POST");
//			} catch (ProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    connection.setRequestProperty("Content-Type",
//		        "application/x-www-form-urlencoded");
//		  //Send request
//		    DataOutputStream wr=null;
//			try {
//				wr = new DataOutputStream (
//				    connection.getOutputStream());
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		    //wr.writeBytes(urlParameters);
//		    try {
//				wr.close();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//		    //Get Response
//		    InputStream is = null;
//			try {
//				is = (InputStream) connection.getInputStream();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
//		    String line;
//		    try {
//				while ((line = rd.readLine()) != null) {
//				  response.append(line);
//				  response.append('\r');
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    try {
//				rd.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    System.out.println(rd.toString());
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

    public String getResponse(String zip)
    {
        String city=null;
        try
        {
            int i;
            if (zip.contains("-"))
            {
                zip = zip.replace("-", "");
                zip =zip.replace(" ", "");
            }
            String responseJson="";
            URL yahoo = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=" + zip);
            URLConnection yc = yahoo.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                responseJson=responseJson+inputLine;
            in.close();
            //System.out.println(responseJson);
            JSONObject obj = new JSONObject(responseJson);
            JSONArray arr = obj.getJSONArray("results");

            for (i=0;i<arr.length();i++)
            {
                if (arr.getJSONObject(i).toString().contains("formatted_address"))
                {
                    break;
                }
            }
            String cityString = arr.getJSONObject(i).getString("formatted_address");
            String[] cityAttributes = cityString.split(",");
            city= cityAttributes[0];

        }
        catch(Exception e)
        {
            //e.printStackTrace();
        }
        return city;
    }

//	public void parse(URL url) throws Exception
//	{
//		Document doc = Jsoup.parse(url, 3000);
//		Elements divs = doc.select("div.multicolumn");
//		String textString=divs.text().replace('(', ';');
//		textString = textString.replace(')',';');
//		textString = textString.replaceAll(" ", "");
//		//System.out.println(textString);
//		String[] listOfValues = textString.split(";");
//		for (int i =0;i<listOfValues.length;i=i+2)
//		{
//			String numberString = listOfValues[i+1];
//			if (numberString.contains("("))
//			{
//				numberString = numberString.replace("(", "");
//			}
//			if (numberString.contains(")"))
//			{
//				numberString = numberString.replace(")", "");
//			}
//			treatmentCenterByState.put(listOfValues[i],Integer.parseInt(numberString));
//		}
//		parseIndividualStates();
//		printoutTable();
//	}

    public void run()
    {
        try {
            city_ToSearch = getResponse(currentZip);
            parseIndividualStates(currentState);

            for(int i = 0; i < finalList.size();++i){

                sendSMS(sender, finalList.get(i).address + "\n "+
                finalList.get(i).city + ", " + finalList.get(i).zipCode+
                "\n" + finalList.get(i).contactnumber);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }



}
