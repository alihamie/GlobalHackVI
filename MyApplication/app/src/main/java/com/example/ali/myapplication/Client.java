package com.example.ali.myapplication;

import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ali on 10/21/2016.
 */

public class Client extends Thread {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = reference.child("number");

    public static final String WELCOMETEXT = "Hi , thank you for reaching out to us . \n" +
            "Lets get started! \n" +
            "what is your Zip code?";


    public static final String LISTOPTIONS ="Please send :\n1- Emergency Shelter Info" +
            "\n2- Shelters Close  By " +
            "\n3- Free Food Near  By " +
            "\n4- Job offer close By ";

    private Object [] pdus;

    int counter = 0;

     int idd = 0;

    public Client(Object [] pdus , int id){

        this.idd = id;
        this.pdus = pdus;
    }

    public int getIdd(){

        return idd;
    }
    public void run() {

        if (pdus.length == 0) {
            return;
        }




    }


    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


    private void addNewUserToDatabase(){



    }

    private  String getPreviousMessage(){

        return "32304";

    }

    //this function queries the database
    //and checks if the user has been registered before
    private void checkSenderNumber(final String sender){


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              if(dataSnapshot.getValue(String.class).equals("0")){
                  sendSMS(sender,WELCOMETEXT);
                  ref.setValue(sender);
                  ++counter;
              }else  if(counter == 0 ){
                  sendSMS(sender,LISTOPTIONS);

              }else{

                  ref.removeEventListener(this);
              }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



}
