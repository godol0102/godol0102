package com.lcm.demo.main;

import com.lcm.demo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Intro extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
				
		Button mButton = (Button) findViewById(R.id.goto_register_page);
		mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intro.this, RegisterService.class));
			}
		});
		
		/* If the user have register the service, the button should be invisible.
		 * Check the uesr registry.
		 * Registered : Go to main screen
		 * Not registred: Show the register button
		 */
		
		
		//Temp. Needs modification
		if(false) {
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
	}


	
}
