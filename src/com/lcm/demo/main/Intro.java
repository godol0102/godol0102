package com.lcm.demo.main;

import java.net.HttpURLConnection;
import java.net.URL;

import com.lcm.demo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Intro extends Activity {

	ProgressDialog connProgress;
	ConnToServerThread connThread;
	private boolean registered = true;
	
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
		
		
		//Temp. Needs modification (check tokens in the local file system)
		
		if (registered) {
			mButton.setVisibility(View.GONE);
		}
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
				Toast.makeText(this, "WCDMA 무선데이터에 연결.. 요금 폭탄 조심 블라 블라", Toast.LENGTH_SHORT).show();
			}
		} else {
			AlertDialog.Builder connAlert = new AlertDialog.Builder(this);
			connAlert.setMessage("네트워크에 연결되어 있지 않습니다. 연결 상태를 확인한 뒤 다시 실행해주세요")
			.setPositiveButton("종료", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.setCancelable(false)
			.show();
		}
		
		if (registered) {
			connThread = new ConnToServerThread("http://www.daum.net");
			connThread.start();
			connProgress = ProgressDialog.show(this, "", "로딩.. 잠시만 기다려주세요.");
		}
	}
	
	void setRegisteredFlag(boolean register) {
		registered = register;
	}
	
	boolean getRegisteredFlag () {
		return registered;
	}
	
	class ConnToServerThread extends Thread {
		String serverUrl;
		String connResult;
		
		public ConnToServerThread(String url) {
			// TODO Auto-generated constructor stub
			serverUrl = url;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				URL mUrl = new URL(serverUrl);
				HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
				if (conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						//Temp. Needs modification
						//Authenticate user and load user data
						sleep(3000);
					}
				}
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			completeConn.sendEmptyMessage(0);
		}
		
		Handler completeConn = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				connProgress.dismiss();
				startActivity(new Intent(Intro.this, DemoLayoutActivity.class));
			}
			
		};
	}
}
