package com.godol2.hhc.c2dm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.godol2.hhc.main.RegisterService;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * @author cm.lim
 * this singleton class is used to keep network related resource
 * such as C2DM, HttpClient; Session, Cookies, etc.
 * 
 * Important Note: Android session may not sync with webview's
 * please don't forget to use cookiesyncmanager class to sync with HttpClient in this class
 * confer to the web link below:
 * http://stackoverflow.com/questions/2566485/webview-and-cookies-on-android
 */
public class ConnectivityUtil {
	public static final String SITE_URL = "http://godol2.cafe24.com";
	public static final String LOGIN_PHP_URL = "/cmlim/c2dm/login_android.php";
	public static final String REG_PHP_URL = "/cmlim/c2dm/c2dm_reg_android.php";
	public static final String HOME_PHP_URL = "/cmlim/index.php";
	
	public static final String SENDER_EMAIL = "godol0102@gmail.com";
	public static final String SHARED_PREF_NAME = "c2dm_pref";
	public static final String REGISTERED = "c2dm_registered";
	public static final String AUTO_LOGIN = "auto_login";
	public static final String PWD = "m_pass";
	public static final String ID = "m_id";
	public static final String REGID = "m_regId";
	private static final String TAG = "ConnectivityUtil";
	
	public static final String RESULT_SUCCESS = "success";
	public static final String RESULT_ID_ERROR = "id_error";
	public static final String RESULT_PWD_ERROR = "pwd_error";
	
	private HttpClient httpClient = null;
	private CookieManager cookieManager = null;
	private static ConnectivityUtil itself = null;
	private String id = "";
	private String pwd = "";
	
	public static ConnectivityUtil getInstance(Context context) {
		if(itself==null) itself = new ConnectivityUtil(context);
		return itself;
	}
	
	private ConnectivityUtil(Context context) {
//		mContext = context;
		httpClient = new DefaultHttpClient();
		CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
		cookieManager = CookieManager.getInstance();
	}

	/**
	 * to send register request to C2DM server
	 */
	public void c2dmRegister(Context context) {
		Log.e(TAG,"c2dmRegister invoked");
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
        registrationIntent.putExtra("sender", SENDER_EMAIL);
        context.startService(registrationIntent);
	}

	/**
	 * after rcved register success msg, following procedure will proceed
	 */
	public void c2dmRegistered(Context context, String registrationID) {
		Log.e(TAG,"c2dmRegistered invoked id: " + registrationID);
		// Send the registration ID to the 3rd party site that is sending the messages.
		// This should be done in a separate thread.
		// When done, remember that all registration is done.
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREF_NAME, Activity.MODE_PRIVATE);
		sharedPreferences.edit().putString(REGID, registrationID).commit();
		ArrayList<NameValuePair> httpData = new ArrayList<NameValuePair>(2);
		String idValue = (sharedPreferences.getString(ConnectivityUtil.ID, "").equals(""))? id:sharedPreferences.getString(ConnectivityUtil.ID, "");
		String pwdValue = (sharedPreferences.getString(ConnectivityUtil.PWD, "").equals(""))? pwd:sharedPreferences.getString(ConnectivityUtil.PWD, "");
        httpData.add(new BasicNameValuePair(ConnectivityUtil.ID, idValue));
        httpData.add(new BasicNameValuePair(ConnectivityUtil.PWD, pwdValue));
        httpData.add(new BasicNameValuePair(ConnectivityUtil.REGID, registrationID));
		String result = sendHttpPostRequest(SITE_URL+REG_PHP_URL, httpData);
		Log.e(TAG,"saved id: " + idValue);
		Log.e(TAG,"saved pwd: " + pwdValue);
		Log.e(TAG,"saved regid: " + sharedPreferences.getString(ConnectivityUtil.REGID, ""));
		Log.e(TAG,"registration result: " + result);
		if(result.contains(RESULT_SUCCESS)) {
			// this indicates that registratin both to c2dm server and to our server has been completed
			Editor editor = sharedPreferences.edit();
			editor.putString(REGISTERED, registrationID).commit();
		}
	}

	public void c2dmUnregister(Context context) {
		Intent unregIntent = new Intent(
				"com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app",
				PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		context.startService(unregIntent);
	}

	public void c2dmUnregistered(Context context) {
		Editor editor = context.getSharedPreferences(SHARED_PREF_NAME,
				Activity.MODE_PRIVATE).edit();
		editor.putBoolean(REGISTERED, false).commit();
	}

	public static boolean isC2dmRegistered(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_PREF_NAME, Activity.MODE_PRIVATE);
		return sharedPreferences.getBoolean(REGISTERED, false);
	}

	// ------------------------------
	// Http Post로 주고 받기
	// ------------------------------
	public String sendHttpPostRequest(String httpUrl, ArrayList<NameValuePair> httpData) {
		// Create a new HttpClient and Post Header
	    HttpPost httppost = new HttpPost(httpUrl);
	    HttpResponse response = null;
	    BasicResponseHandler basicResponseHandler = new BasicResponseHandler();
	    String endResult = "";
	    try {
	    	// Add data
	        httppost.setEntity(new UrlEncodedFormEntity(httpData));
	        // Execute HTTP Post Request
	        response = httpClient.execute(httppost);
	        endResult  = basicResponseHandler.handleResponse(response);
	    } catch (ClientProtocolException e) { e.printStackTrace(); } 
	    catch (IOException e) { e.printStackTrace(); }
	    return endResult;
	}
	
	/*
	 * after invoke this method, session should be kept
	 */
	public boolean loginRequest(Context context, String idValue, String pwdValue) {
		boolean successful = false;
		id = idValue; pwd = pwdValue;
		ArrayList<NameValuePair> httpData = new ArrayList<NameValuePair>(2);
        httpData.add(new BasicNameValuePair(ID, idValue));
        httpData.add(new BasicNameValuePair(PWD, pwdValue));
		
        String result = sendHttpPostRequest(SITE_URL+LOGIN_PHP_URL, httpData);
        Log.e(TAG,"Http Post Result: " + result);
        if(result.contains(RESULT_SUCCESS)) {
        	// if login was successful
        	successful = true;
        
	        List<Cookie> cookies = ((DefaultHttpClient)httpClient).getCookieStore().getCookies();
	        if (!cookies.isEmpty()) {
	            for (int i = 0; i < cookies.size(); i++) {
	                // cookie = cookies.get(i);
	                String cookieString = cookies.get(i).getName() + "="
	                        + cookies.get(i).getValue();
	                Log.e(TAG, cookieString); // this cookie contains SESSION_ID
	                cookieManager.setCookie(SITE_URL, cookieString);
	            }
	        }
        }
        // this code block is for registering device id to our server
		boolean serverStoreRegId = false;
        
        String deviceRegId = context.getSharedPreferences(SHARED_PREF_NAME, Activity.MODE_PRIVATE).getString(REGISTERED, "");
        
        String[] serverStoredId = result.split("c2dm");
        String serverIds = "none";
        if(serverStoredId.length>1)
        	if (serverStoredId[1].contains(deviceRegId)) {
        		serverStoreRegId = true;
        		serverIds = serverStoredId[1];
        	}
        Log.e(TAG,"server stored id: " + serverIds + " deviceRegId: " + deviceRegId);
        // this code block end
        
        if(deviceRegId.equals("") || serverStoreRegId ) {
        	// if there's no registration id in this device 
        	// or if device's reg id doesn't exist we should c2dm registration process
        	c2dmRegister(context);
        }
        return successful;
	}

	// 서버에서 전송된 XML 데이터를 파싱하기 위한 메서드
	// 이 예제에서는 서버에서 로그인이 성공하는 경우(id=kim&passwd=111)하는 경우
	// <result>success</result>
	// 실패하는 경우 <result>failed</result>를 반환하도록 설정해 두었다.
	public String parsingData(InputStream input) {
		String result = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new InputStreamReader(input));
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				if (name != null && name.equals("result"))
					result = parser.nextText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public CookieManager getCookieManager() {
		return cookieManager;
	}

}
