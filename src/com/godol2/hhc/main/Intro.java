package com.godol2.hhc.main;


import com.godol2.hhc.c2dm.ConnectivityUtil;
import com.godol2.hhc.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Intro extends Activity {
	private static final String TAG = "Intro";

	ProgressDialog connProgress;
	ConnToServerThread connThread;
	private boolean registered = true;
	
	// login related variables
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
				
		Button mButton = (Button) findViewById(R.id.goto_register_page);
		mButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intro.this, RegisterService.class));
			}
		});
		
		/* If the user has register the service, the button should be invisible.
		 * Check the user registry.
		 * Registered : Go to main screen
		 * Not registred: Show the register button
		 */
		
		// the button will be invisible in case auto login is enabled
		sharedPreferences = getSharedPreferences(ConnectivityUtil.SHARED_PREF_NAME, Activity.MODE_PRIVATE);
		if (sharedPreferences.getBoolean(ConnectivityUtil.AUTO_LOGIN, false)) {
			mButton.setVisibility(View.GONE);
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		/* Check the network status
		 * 
		 */
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
		
		if (netInfo != null) {
			if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				/* Show the alarm for network usage
				 * 
				 */
				Toast.makeText(this, "WCDMA Î¨¥ÏÑ†?∞Ïù¥?∞Ïóê ?∞Í≤∞.. ?îÍ∏à ??ÉÑ Ï°∞Ïã¨ Î∏îÎùº Î∏îÎùº", Toast.LENGTH_SHORT).show();
			}
		} else {
			AlertDialog.Builder connAlert = new AlertDialog.Builder(this);
			connAlert.setMessage("?§Ìä∏?åÌÅ¨???∞Í≤∞?òÏñ¥ ?àÏ? ?äÏäµ?àÎã§. ?∞Í≤∞ ?ÅÌÉúÎ•??ïÏù∏?????§Ïãú ?§Ìñâ?¥Ï£º?∏Ïöî")
			.setPositiveButton("Ï¢ÖÎ£å", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.setCancelable(false)
			.show();
		}
		
		// let's login in case auto login is enabled.
		if(sharedPreferences.getBoolean(ConnectivityUtil.AUTO_LOGIN, false)) {
			// TODO: need to login to the server by sending POST request with ID & PWD
			// and keep the session so that the application stays logged in

// to keep session
//			1. æ€ø°º≠ login Ω√ø° httpClientø°º≠ ƒÌ≈∞¡§∫∏∏¶ ∞°¡ˆ∞Ì ø¬»ƒ, setCookieStore()∏¶ ≈Î«ÿº≠ ƒÌ≈∞∏¶ ¿˙¿Â«’¥œ¥Ÿ.
//			2. webviewø°º≠¥¬ CookieManagerø°º≠ ¿˙¿Âµ» ƒÌ≈∞¡§∫∏∏¶ ∫“∑ØøÕ  setCookie()¿ª ≈Î«ÿº≠ ƒÌ≈∞ ¡§∫∏∏¶ ¿˙¿Â«—»ƒ
//			CookieSyncManager()∏¶ ≈Î«ÿº≠ ΩÃ≈©∏¶ «ÿ¡›¥œ¥Ÿ
			
			connThread = new ConnToServerThread();
			connThread.start();
//			connThread = new ConnToServerThread("http://www.daum.net");
//			connThread.start();
//			connProgress = ProgressDialog.show(this, "", "Î°úÎî©.. ?†ÏãúÎß?Í∏∞Îã§?§Ï£º?∏Ïöî.");
		}
	}
	
	class ConnToServerThread extends Thread {
		String connResult;

		@Override
		public void run() {
			boolean isLoggedIn = false;
			boolean autoLogin = sharedPreferences.getBoolean(ConnectivityUtil.AUTO_LOGIN, false);
			Log.e(TAG,"autoLogin: " + autoLogin);
			if(autoLogin) {
				// if auto login is set, login process is required
				String id = sharedPreferences.getString(ConnectivityUtil.ID, "");
				String pwd = sharedPreferences.getString(ConnectivityUtil.PWD, "");
				
				ConnectivityUtil connectivityUtil = ConnectivityUtil.getInstance(Intro.this);
		        if(connectivityUtil.loginRequest(Intro.this,id,pwd)) isLoggedIn = true; // login was successful
			}
	        Message msg = new Message();
	        // if login was successful start DemoLayoutActivity
	        if(isLoggedIn) msg.obj = new Intent(Intro.this,DemoLayoutActivity.class);
	        // otherwise start register page so that user can log in manually or can sign up
	        else msg.obj = new Intent(Intro.this,RegisterService.class);
			completeConn.sendMessage(msg);
		}
		
		Handler completeConn = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				connProgress.dismiss();
				startActivity((Intent)msg.obj);
			}
		};
	}
}
