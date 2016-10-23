package com.example.ali.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TextService extends Service  implements GeoCoding.resultCallBack{
    public TextService() {
    }


    public static int STEP = 0;

    public static final String WELCOMETEXT = "Hi , thank you for reaching out to us . \n" +
            "Lets get started! \n" +
            "what is your Zip code?";


    public static final String LISTOPTIONS ="Please send :\n1- Rental Assistance Program" +
            "\n2- Emergency Shelters Close  By " +
            "\n3- Free Food Near  By " +
            "\n4- Job offer close By " +
            "\n5- Free Medical and Treatment Center";


    // Get the object of SmsManager

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private  int counter = 0;
    public String currentZipCode = "";
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.d("Ali","Created service!");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(textReceiver,filter);
    }

    @Override
    public String getFinalResult(String result,int requestCode,String sender) {
            String city = result.split(">")[0];
            String state = result.split(">")[1];
            String stateFull = result.split(">")[2];
            if(requestCode == 3){

                new Thread(new SoupKitchenParse(sender,city,state)).start();

            }else if(requestCode == 5){

                new Thread(new TreatmentCenterParser(stateFull,currentZipCode,getApplicationContext(),sender)).start();

            }else if(requestCode == 2){

                new Thread(new ParseShelters(city,state,sender)).start();
            }
        return  result ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Ali","Service destroyed!!");
        unregisterReceiver(textReceiver);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = reference.child("phone");
    //this function queries the database
    //and checks if the user has been registered before
    private void checkSenderNumber(final String sender,final String msg){


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFound = false;
                String senderZip = "";
                for(DataSnapshot data :  dataSnapshot.getChildren()){
                    if(data.getKey().equals(sender)) {
                        isFound = true;
                        senderZip = String.valueOf(data.child("location").getValue(Long.class));
                        currentZipCode = senderZip;
                        Log.d("ZIP", "ZIP is " + senderZip);
                    }
                }

                if(!isFound)
                {
                    ref.child(sender).setValue(new User("NULL",0000));
                    sendSMS(sender,WELCOMETEXT);
                    ++counter;
                    STEP = 2;
                }
                else if(msg.length() == 5  ){

                    ref.child(sender).setValue(new User("NULL",Long.valueOf(msg)));
                    currentZipCode = msg;
                    sendSMS(sender,LISTOPTIONS);
                }
                else if(isFound && counter == 0){

                    if(msg.contains("1")){
                        new Thread(new WebParses(sender)).start();

                    }else if(msg.contains("2")){
                        GeoCoding.getResult(mContext,2,sender,senderZip);
                    }else if(msg.contains("3")){
                        GeoCoding.getResult(mContext,3,sender,senderZip);
                    }else if(msg.contains("4")){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    checkForJobs(sender);
                                }
                            }).start();
                    }else if(msg.contains("5")){
                        GeoCoding.getResult(mContext,5,sender,senderZip);
                    }else{

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendSMS(sender, LISTOPTIONS);
                            }
                        }).start();
                    }


                }

                ref.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void checkForJobs( final String sender){
        DatabaseReference jobref = reference.child("service");

        jobref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data :  dataSnapshot.getChildren()){

                        sendSMS( sender,"JOB: " + data.getKey().toString()
                        +"\nWhen? " +data.child("phone_number").getValue(String.class)
                                +"\nWhere? " + data.child("lname").getValue(String.class));


                    }
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private final BroadcastReceiver textReceiver =  new BroadcastReceiver() {



        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    // get sms objects
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus.length == 0) {
                        return;
                    }


                    // large message might be broken into many
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sb.append(messages[i].getMessageBody());
                    }
                    String sender = messages[0].getOriginatingAddress();
                    String message = sb.toString();
                    checkSenderNumber(sender,message);
                    // getDataEntry();
                    counter = 0;
                    Log.e("Ali","Receiver message is : " + message);

                }
            }
        }




        private void addNewUserToDatabase(){



        }

        private  String getPreviousMessage(){

            return "32304";

        }


        private void getDataEntry(){

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot data : dataSnapshot.getChildren() ){
                        long location = (long) data.child("location").getValue();
                        Log.d("Database", "" + location);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


    };



}
