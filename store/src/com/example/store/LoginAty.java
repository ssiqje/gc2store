package com.example.store;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.store.network.Network_util;
import com.example.view_component.viewComponent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginAty extends Activity {
	private EditText edittext_login_user_name, edittext_login_user_psw;
	private CheckBox checkbox_login_auto_login;
	private boolean auto_login;
	private Handler handler;
	private Dialog waitDialog;
	private SharedPreferences spPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		edittext_login_user_name = (EditText) findViewById(R.id.edittext_login_user_name);
		edittext_login_user_psw = (EditText) findViewById(R.id.edittext_login_user_psw);
		checkbox_login_auto_login = (CheckBox) findViewById(R.id.checkbox_login_auto_login);
		auto_login=false;
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==CodeId.MessageID.LOGIN_USER_PASS)
				{
					boolean b = false;
					spPreferences= LoginAty.this.getSharedPreferences("user", MODE_APPEND);
					Editor editor = spPreferences.edit();
					String old_user_json = spPreferences.getString("user_json", "");
					JSONObject user_jsonJsonObject=null;
					try {
						user_jsonJsonObject = new JSONObject(msg.obj.toString());
						if(auto_login)
						{
							
							editor.putBoolean("auto_login", true);
							editor.putString("user_json", user_jsonJsonObject.toString());
							editor.commit();
							
						}else {
							user_jsonJsonObject.put("user_psw", "");
							editor.putBoolean("auto_login", false);
							editor.putString("user_json", user_jsonJsonObject.toString());
							editor.commit();
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					Intent intent = new Intent();
					try {
						if("".equals(old_user_json))
						{
							b=false;
						}else {
							JSONObject oldJsonObject = new JSONObject(old_user_json);
							if(oldJsonObject.getInt("id")==user_jsonJsonObject.getInt("id"))
							{
								b=true;
							}else {
								b=false;
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					intent.putExtra("user_json",user_jsonJsonObject.toString());
					intent.putExtra("is_same", b);
					
					LoginAty.this.setResult(Activity.RESULT_OK, intent);
					waitDialog.dismiss();
					finish();
				}
				if(msg.what==CodeId.MessageID.LOGIN_USER_FAIL)
				{
					waitDialog.dismiss();
					new AlertDialog.Builder(LoginAty.this).setTitle("抱歉！").setMessage(msg.obj.toString()).setNeutralButton("确定", null).create().show();
				}
				
			}
		};
		
		
	}

	// 登入按扭事件
	public void logIn(View v) {
		int id = 0;
		try {
			id = Integer.parseInt(edittext_login_user_name.getText().toString());
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			new AlertDialog.Builder(LoginAty.this).setTitle("提示！").setMessage("通行ID只能是数字，请你重新输入！").setCancelable(false).setNeutralButton("确定", null).create().show();
			e1.printStackTrace();
			return;
		}
		String user_psw = edittext_login_user_psw.getText().toString();
		auto_login = checkbox_login_auto_login.isChecked();
		JSONObject userJsonObject = new JSONObject();
		try {
			userJsonObject.put("id", id);
			userJsonObject.put("user_psw", user_psw);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waitDialog = viewComponent.waitServerGetSomeThing(LoginAty.this, "请稍候。。。","正在登入中，请稍后。。。");
		Network_util.login_user(handler, userJsonObject.toString());
	}

	// 取消登入按扭事件
	public void cancel(View v) {
		finish();
	}
}
