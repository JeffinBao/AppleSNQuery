package com.applesnquery.app;

import com.thinkland.sdk.android.SDKInitializer;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	private static Context context;
	
	public void onCreate(){
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		context=getApplicationContext();
	}

	public static Context getContext(){
		return context;
	}
}
