package com.godol2.hhc.contact;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable{
	
	final String NS = "http://schemas.android.com/apk/res/com.godol2.hhc";
	final String ATTR = "checkable";
	
	int checkableId;
	Checkable checkable;
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		checkableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
	}
	
	@Override
	public boolean isChecked() {
		checkable = (Checkable) findViewById(checkableId);
		if(checkable == null)
			return false;
		return checkable.isChecked();
	}
	@Override
	public void setChecked(boolean checked) {
		checkable = (Checkable) findViewById(checkableId);
		if(checkable == null)
			return;
		checkable.setChecked(checked);
	}
	@Override
	public void toggle() {
		checkable = (Checkable) findViewById(checkableId);
		if(checkable == null)
			return;
		checkable.toggle();
	}
}