package com.godol2.hhc.main;

import com.godol2.hhc.c2dm.ConnectivityUtil;
import com.godol2.hhc.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterService extends Activity {
	
	protected static final String TAG = "RegisterService";

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
//				pwd = DigestUtils.sha512Hex(pwd); // this needs to be processed
				boolean autoLogin = ((CheckBox)findViewById(R.id.auto_login_checkbox)).isChecked();

				ConnectivityUtil connectivityUtil = ConnectivityUtil.getInstance(RegisterService.this);
				// save encrypted Pwd in case Auto Login is checked
				SharedPreferences sharedPreferences = getSharedPreferences(ConnectivityUtil.SHARED_PREF_NAME, Activity.MODE_PRIVATE);
				
				boolean loginSuccessful = connectivityUtil.loginRequest(RegisterService.this,id, pwd);
				
				if(autoLogin && loginSuccessful) {
					// if log in process has been successful and user set auto login box checked
					// this configuration should be stored.
					// TODO: Important: we need to fortify the security regarding password
					sharedPreferences.edit().putBoolean(ConnectivityUtil.AUTO_LOGIN, true);
					sharedPreferences.edit().putString(ConnectivityUtil.ID, id);
					sharedPreferences.edit().putString(ConnectivityUtil.PWD, pwd);
					sharedPreferences.edit().commit();
				}
				if(loginSuccessful) {
					// if the log in has been successful, need to go to the next screen.
					// otherwise, the programme should notify user the log in failed.
					Toast.makeText(RegisterService.this, "Log In successful", Toast.LENGTH_SHORT).show();
//					startActivity(new Intent(RegisterService.this,DemoLayoutActivity.class));
				} else {
					Toast.makeText(RegisterService.this, "Wrong ID or Password, Try again", Toast.LENGTH_SHORT).show();
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
				//startActivity(new Intent(RegisterService.this, ?????.class));
				// TODO: need to log in and do the registration process
			}
		});
	}
	
}
