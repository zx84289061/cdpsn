package com.example.cdpsn;


import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;


public class ExampleApplication extends Application {
    private static final String TAG = "ExampleApplication";

    @Override
    public void onCreate() {
    	 Log.d(TAG, "onCreate");
         super.onCreate();
         JPushInterface.setDebugMode(true); 	
         JPushInterface.init(this);     		
    }
}
