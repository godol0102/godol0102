package com.godol2.hhc.func1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.godol2.hhc.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class Func1 extends Activity {

	ImageView img;
	String imgUrl;
	private static String TAG = "Func1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		imgUrl = "http://fmkorea.net/files/attach/images/3772267/293/009/004/%EC%84%9C%ED%98%84.jpg";
		int idx = imgUrl.lastIndexOf('/');
		String localImg = imgUrl.substring(idx + 1);
		String path = Environment.getDataDirectory().getAbsolutePath();
		path += "/data/com.lcm.demo/files/" + localImg;
		
		if (new File(path).exists() == false) {
			downloadImg(imgUrl, localImg);
		}
		
		setContentView(R.layout.func1);
		
		img = (ImageView) findViewById(R.id.img_from_server);
	
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			if (bitmap != null) {
				Log.i(TAG, "img null =" + img);
				img.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void downloadImg(String url, String FileName) {
		URL imageUrl;
		int read;
		try {
			imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			if (conn != null) {
				int len = conn.getContentLength();
				byte[] data = new byte[len];
				InputStream is = conn.getInputStream();
				FileOutputStream fos = openFileOutput(FileName, 0);
				
				for (;;) {
					read = is.read(data);
					if (read <= 0) {
						break;
					}
					fos.write(data, 0, read);
				}
				is.close();
				fos.close();
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
