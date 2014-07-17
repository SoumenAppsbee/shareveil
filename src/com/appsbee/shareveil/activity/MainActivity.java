package com.appsbee.shareveil.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.appsbee.shareveil.R;

public class MainActivity extends Activity
{
	private WebView webView;
	private final String BASE_URL = "http://sulavmart.com/";
	private String public_key,private_key,password = null;
	private SharedPreferences pref;
	private String android_id;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		TrackLocation.tackInstance(this);
		
		android_id = Secure.getString(getContentResolver(),Secure.ANDROID_ID); 
		
		pref = getSharedPreferences(Constant.Values.USER_PREF.name(), Context.MODE_PRIVATE);
		
		webView = (WebView) findViewById(R.id.webView);
		webView.setSaveEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setWebChromeClient(new WebChromeClient());

		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				System.out.println("@@-current url "+url);
				Log.e("@@-current url", " "+url);
            	if(!url.contains("js-frame:SAVEKEY")){
            		view.loadUrl(url);
            	}
            	
            	if(url.contains("js-frame:SAVEKEY")){	        		
	        		String values = url.toString();
            		String[] separated = values.split(":");
            		private_key = separated[2];
            		public_key = separated[3];
            		password = separated[4]; 
            		
            		System.out.println("!--public_key"+public_key);
            		System.out.println("!--private_key"+private_key);
            		System.out.println("!--password"+password);
            		
	        	    Intent i = new Intent(getApplicationContext(), ShareEvilAlert.class);
	        	    	Bundle bundle = new Bundle();
	        	    	bundle.putString("public_key", public_key);
	        	    	bundle.putString("private_key", private_key);
	        	    	bundle.putString("password", password);
	        	    	
	        	    	i.putExtra("info", bundle);
	            	    startActivity(i);
	            	}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{

				super.onPageStarted(view, url, favicon);
				Log.d("onPageStarted url", "" + url);

			}

			@Override
			public void onReceivedSslError(WebView view,SslErrorHandler handler, SslError error)
			{
				super.onReceivedSslError(view, handler, error);
				handler.proceed();
			}

			@Override
			public void onLoadResource(WebView view, String url)
			{
				super.onLoadResource(view, url);
			}

			@SuppressLint("NewApi")
			@Override
			public void onPageFinished(WebView view, final String url)
			{
				if(url.contains("http://sulavmart.com/home/read/") || url.contains("http://sulavmart.com/home/compose")){
					injectJavaScript();
				}
				if(url.contains("http://sulavmart.com/home/signup")){
					sendCurrentLatLong();
				}
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl)
			{
			}
		});

		webView.loadUrl(BASE_URL);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.clearCache(false);
	}
	
	@SuppressLint("NewApi")
	private void injectJavaScript()
	{
			String private_key = pref.getString(Constant.Values.PRIVATEKEY.name(), null);
			String public_key = pref.getString(Constant.Values.PUBLICKEY.name(), null);
			String password = pref.getString(Constant.Values.PASSWORD.name(), null);

			String javaScript = "javascript:getKeysWrapper('"+ private_key + "'" + ",'" + public_key + "'" + ",'" + password + "'" + ")";
			
			System.out.println("@--javaScript"+javaScript);

			if (Build.VERSION.SDK_INT >= 19)
			{
				//webView.evaluateJavascript(javaScript, null);
				Log.d(getClass().getSimpleName(),
						"callMyFlightNotification Javascript Executed");
				Log.d(getClass().getSimpleName(),
						"callMyFlightNotification script:\n" + javaScript);
			}
			else
			{
				webView.loadUrl(javaScript);
				Log.d(getClass().getSimpleName(),
						"callMyFlightNotifications Javascript Executed");
				Log.d(getClass().getSimpleName(),
						"callMyFlightNotification script:\n" + javaScript);
			}
		}
	
	@SuppressLint("NewApi")
	private void sendCurrentLatLong() {
		
		String javaScript = "javascript:getDeviceDetailsWrapper('"+ android_id + "'" + ",'" + Constant.lat + "'" + ",'" + Constant.lon + "'" + ")";
		
		System.out.println("!--javaScript"+javaScript);

		if (Build.VERSION.SDK_INT >= 19)
		{
			//webView.evaluateJavascript(javaScript, null);
			Log.d(getClass().getSimpleName(),
					"callMyFlightNotification Javascript Executed");
			Log.d(getClass().getSimpleName(),
					"callMyFlightNotification script:\n" + javaScript);
		}
		else
		{
			webView.loadUrl(javaScript);
			Log.d(getClass().getSimpleName(),
					"callMyFlightNotifications Javascript Executed");
			Log.d(getClass().getSimpleName(),
					"callMyFlightNotification script:\n" + javaScript);
		}
	}
	
	@Override
	public void onBackPressed()
	{
		if (webView.canGoBack())
		{
			webView.goBack();
		}
		else
			super.onBackPressed();
	}
}
