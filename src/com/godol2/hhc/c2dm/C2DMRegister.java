package com.godol2.hhc.c2dm;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class C2DMRegister {
	private Context mContext;
	public static String SENDER_EMAIL = "godol2@gmail.com";
	public static String SHARED_PREF_NAME = "c2dm_pref";
	public static String REGISTERED = "c2dm_registered";
	public static String AUTO_LOGIN = "auto_login";
	public static String PWD = "pwd";
	public static String ID = "id";
	
	public C2DMRegister(Context context) {
		mContext = context;
	}
	
	/**
	 * to send register request to C2DM server
	 */
	public void register() {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(mContext, 0, new Intent(), 0)); // boilerplate
		registrationIntent.putExtra("sender", SENDER_EMAIL);
		mContext.startService(registrationIntent);
	}
	
	/**
	 * after rcved register success msg,
	 * following procedure will proceed
	 */
	public void registered() {
		Editor editor = mContext.getSharedPreferences(SHARED_PREF_NAME, Activity.MODE_PRIVATE).edit();
		editor.putBoolean(REGISTERED, true).commit();
		
		// Send the registration ID to the 3rd party site that is sending the messages.
		// This should be done in a separate thread.
		// When done, remember that all registration is done.
		// TODO: should send POST request asking server save registration_ID
	}
	
	public void unregister() {
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(mContext, 0, new Intent(), 0));
		mContext.startService(unregIntent);
	}
	
	public void unregistered() {
		Editor editor = mContext.getSharedPreferences(SHARED_PREF_NAME, Activity.MODE_PRIVATE).edit();
		editor.putBoolean(REGISTERED, false).commit();
	}
	
	public static boolean isRegistered(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Activity.MODE_PRIVATE);
		return sharedPreferences.getBoolean(REGISTERED, false);
	}
}
