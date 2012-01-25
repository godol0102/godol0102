package com.godol2.hhc.c2dm;

import com.godol2.hhc.main.DemoLayoutActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class C2DMReceiver extends BroadcastReceiver {

	private static final String TAG = "C2DMReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(
				"com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(context, intent);
		} else if (intent.getAction().equals(
				"com.google.android.c2dm.intent.RECEIVE")) {
			handleMessage(context, intent);
		}
	}

	private void handleMessage(Context context, Intent intent) {
		Log.e(TAG,"handle message");
		// TODO: should retrieve data from the server
		PendingIntent pi = PendingIntent.getActivity(context, 0,
				new Intent(context, DemoLayoutActivity.class), 0);
		Notification notification = new Notification(android.R.drawable.ic_input_add,
				"Push Message!!!!", System.currentTimeMillis());
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notification.setLatestEventInfo(context, "push title", "push content", pi);
		notificationManager.notify(0, notification);
		Toast.makeText(context, "You've got a Push Message", Toast.LENGTH_SHORT).show();
	}

	private void handleRegistration(Context context, Intent intent) {
		Log.e(TAG,"handleRegistration");
		String registration = intent.getStringExtra("registration_id");
		if (intent.getStringExtra("error") != null) {
			// Registration failed, should try again later.
		} else if (intent.getStringExtra("unregistered") != null) {
			// unregistration done, new messages from the authorized sender will be rejected
		} else if (registration != null) {
			ConnectivityUtil c2dmRegister = ConnectivityUtil.getInstance(context);
			c2dmRegister.c2dmRegistered(context,registration);
		}
	}

}
