//package com.example.ali.myapplication;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.telephony.SmsManager;
//import android.telephony.SmsMessage;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//
//public class TextReceiver extends BroadcastReceiver{
//
//
//    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//    DatabaseReference ref = reference.child("phone");
//
//    public static final String WELCOMETEXT = "Hi , thank you for reaching out to us . \n" +
//            "Lets get started! \n" +
//            "what is your Zip code?";
//
//
//    public static final String LISTOPTIONS ="Please send :\n1- Rental Assistance Program" +
//            "\n2- Shelters Close  By " +
//            "\n3- Free Food Near  By " +
//            "\n4- Job offer close By ";
//
//    private Context c;
//
//    public TextReceiver() {
//    }
//    // Get the object of SmsManager
//
//    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
//    private  int counter = 0;
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(SMS_RECEIVED)) {
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                // get sms objects
//                Object[] pdus = (Object[]) bundle.get("pdus");
//                if (pdus.length == 0) {
//                    return;
//                }
//                c = context;
//
//                // large message might be broken into many
//                SmsMessage[] messages = new SmsMessage[pdus.length];
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < pdus.length; i++) {
//                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    sb.append(messages[i].getMessageBody());
//                }
//                String sender = messages[0].getOriginatingAddress();
//                String message = sb.toString();
//                checkSenderNumber(sender,message);
//               // getDataEntry();
//                Log.e("Receiver","BroadCast Exiting");
//
//            }
//        }
//    }
//
//
//    private void sendSMS(String phoneNumber, String message) {
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, null, null);
//    }
//
//
//    private void addNewUserToDatabase(){
//
//
//
//    }
//
//    private  String getPreviousMessage(){
//
//        return "32304";
//
//    }
//
//
//    private void getDataEntry(){
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot data : dataSnapshot.getChildren() ){
//                    long location = (long) data.child("location").getValue();
//                    Log.d("Database", "" + location);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
//    //this function queries the database
//    //and checks if the user has been registered before
//    private void checkSenderNumber(final String sender,final String msg){
//
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                boolean isFound = false;
//
//                for(DataSnapshot data :  dataSnapshot.getChildren()){
//                    if(data.getKey().equals(sender))
//                        isFound = true;
//
//                    Log.d("Key",data.getKey());
//
//                }
//
//
//                if(!isFound)
//                {
//                    sendSMS(sender,WELCOMETEXT);
//                    ref.child(sender).setValue(new User("NULL",0000));
//                    ++counter;
//                }
//                else if(msg.matches("^\\d{5}(?:[-\\s]\\d{4})?$")){
//
//                    ref.child(sender).setValue(new User("Null",Long.valueOf(msg)));
//                    sendSMS(sender,LISTOPTIONS);
//                }
//                else if(isFound && counter == 0 ){
//
//                    if(msg.contains("1")){
//                        new Thread(new WebParses(sender)).start();
//
//                    }else if(msg.contains("2")){
//
//                    }else if(msg.contains("3")){
//
//                        GeoCoding.getResult(c);
//                       // new Thread(new SoupKitchenParse(sender,"miami","FL")).start();
//                    }else if(msg.contains("4")){
//
//                    }else if(msg.contains("5")){
//
//                    }else{
//
//                        sendSMS(sender,"Sorry I didn't get that\n" +LISTOPTIONS);
//                    }
//
//
//                }
//
//                ref.removeEventListener(this);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
//
//}
