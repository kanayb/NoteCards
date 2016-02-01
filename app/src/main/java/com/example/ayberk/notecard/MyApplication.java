package com.example.ayberk.notecard;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by ayberk on 11/9/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "YCOJlalGiC38aBwrDwOUQTmbm0xw5WovD6XyrnL3", "iTLkpUf6cnRFn3WUEPky8DKP0aXRpGwnsbVKDTJS");    }


}
