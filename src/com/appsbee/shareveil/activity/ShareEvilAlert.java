package com.appsbee.shareveil.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.appsbee.shareveil.R;

public class ShareEvilAlert extends Activity{
	private Button btn_save;
	private SharedPreferences sharedpref;
	public String type;
	private String public_key,private_key,password = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diaglogue_share);
		
		Bundle bundle = getIntent().getExtras().getBundle("info");		
		
		public_key = bundle.getString("public_key");
		private_key = bundle.getString("private_key");
		password = bundle.getString("password");
		
		System.out.println("!--public_key"+public_key);
		System.out.println("!--private_key"+private_key);
		System.out.println("@@@--password"+password);

		sharedpref = getSharedPreferences(Constant.Values.USER_PREF.name(), Context.MODE_PRIVATE);
		btn_save = (Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor edit = sharedpref.edit();
				edit.putString(Constant.Values.PUBLICKEY.name(), public_key);
				edit.putString(Constant.Values.PRIVATEKEY.name(), private_key);
				edit.putString(Constant.Values.PASSWORD.name(), password);
				edit.commit();
				
				Toast.makeText(ShareEvilAlert.this, "Password is = "+sharedpref.getString(Constant.Values.PASSWORD.name(), null), Toast.LENGTH_LONG).show();
				
				finish();
			}
		});
	}
}
