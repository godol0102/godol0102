package com.godol2.hhc.func2;

import org.apache.http.client.HttpClient;

import com.godol2.hhc.R;
import com.godol2.hhc.c2dm.ConnectivityUtil;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

public class Func2 extends Activity {
	ConnectivityUtil connectivityUtil;
	HttpClient httpClient;
	CookieManager cookieManager;
	CookieSyncManager cookieSyncManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.func2);
		
		connectivityUtil = connectivityUtil.getInstance(this);
		
		WebView webView = (WebView) findViewById(R.id.func21_webview);
		cookieSyncManager = CookieSyncManager.getInstance();
		httpClient = connectivityUtil.getHttpClient();
		cookieManager = connectivityUtil.getCookieManager();
		
		cookieSyncManager.startSync();
		cookieSyncManager.sync();
		webView.loadUrl(ConnectivityUtil.SITE_URL + ConnectivityUtil.HOME_PHP_URL);
	}

	@Override
	protected void onPause() {
		cookieSyncManager.stopSync();
		super.onPause();
	}
	
}
