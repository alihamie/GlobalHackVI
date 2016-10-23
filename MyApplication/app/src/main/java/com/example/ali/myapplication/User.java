package com.example.ali.myapplication;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ali on 10/22/2016.
 */
@IgnoreExtraProperties
public class User {


    public User(){

    }

    public User(String name,long location) {
        this.name = name;
        this.location = location;
    }

    public String name;
    public long location;



}
