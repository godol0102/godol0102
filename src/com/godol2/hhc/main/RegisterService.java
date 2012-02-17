package com.godol2.hhc.main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

import com.godol2.hhc.R;
import com.godol2.hhc.c2dm.C2DMRegister;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class RegisterService extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerservice);

		Button loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String id = ((EditText) findViewById(R.id.id_edittext)).getText().toString();
				String pwd = ((EditText) findViewById(R.id.pwd_edittext)).getText().toString();
				String encryptedPwd = DigestUtils.sha512Hex(pwd);
				boolean autoLogin = ((CheckBox)findViewById(R.id.auto_login_checkbox)).isChecked();
				
				// TODO: need to send Post request including Id and Pwd
				
				if(autoLogin) {
					// save encrypted Pwd in case Auto Login is checked
					SharedPreferences sharedPreferences = getSharedPreferences(C2DMRegister.SHARED_PREF_NAME, Activity.MODE_PRIVATE);
					sharedPreferences.edit().putBoolean(C2DMRegister.AUTO_LOGIN, true).commit();
					sharedPreferences.edit().putString(C2DMRegister.ID, id).commit();
					sharedPreferences.edit().putString(C2DMRegister.PWD, encryptedPwd).commit();
				}
			}
		});
		
		Button signInButton = (Button) findViewById(R.id.signin_button);
		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/* TODO: After filling out the form, send the request to the server
				 * The server chekcks the received information and decides the register. 
				 * Finally, the server sends the confirm message to the client,
				 * the client launches the main screen.  
				 */
				//startActivity(new Intent(RegisterService.this, DemoLayoutActivity.class));
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	

	@Override
	protected void onResume() {
		super.onResume();
	}

	
	
}
