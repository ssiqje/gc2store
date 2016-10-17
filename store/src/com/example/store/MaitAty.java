package com.example.store;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MaitAty extends Activity {
	SharedPreferences p ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		p= this.getSharedPreferences("user", MODE_APPEND);
		boolean first= p.getBoolean("first", true);
		if(first)
		{
			
			startActivity(new Intent(this, WelcomeAty.class));
			finish();
		}
		else 
		{
			startActivity(new Intent(this, HomeActivity.class));
			finish();
		}
	}

}
