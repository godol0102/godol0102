package com.lcm.demo.main;

import com.lcm.demo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterService extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerservice);
		
		Button mButton = (Button) findViewById(R.id.register_request);
		mButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/* After filling out the form, send the request to the server
				 * The server chekcks the received information and decides the register. 
				 * Finally, the server sends the confirm message to the client,
				 * the client launches the main screen.  
				 */
				
				startActivity(new Intent(RegisterService.this, DemoLayoutActivity.class));
			}
		});
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
	}

	
	
}
