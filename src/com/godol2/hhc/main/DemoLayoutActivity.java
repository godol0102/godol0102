package com.godol2.hhc.main;

import com.godol2.hhc.func1.Func1;
import com.godol2.hhc.func2.Func2;
import com.godol2.hhc.func3.Func3;
import com.godol2.hhc.func4.Func4;
import com.godol2.hhc.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class DemoLayoutActivity extends ActivityGroup {
	private TabHost tabHost;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.main_screen);
        
        tabHost = (TabHost)findViewById(R.id.tabHost);
        
        tabHost.setup(getLocalActivityManager());
        
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab1)).
        		setIndicator(getString(R.string.tab1)).setContent(new Intent(this, Func1.class)));
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab2)).
        		setIndicator(getString(R.string.tab2)).setContent(new Intent(this, Func2.class)));
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab3)).
        		setIndicator(getString(R.string.tab3)).setContent(new Intent(this, Func3.class)));
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab4)).
        		setIndicator(getString(R.string.tab4)).setContent(new Intent(this, Func4.class)));
        
    }
    
    
}
