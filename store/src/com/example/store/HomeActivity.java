package com.example.store;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store.bean.Summary;
import com.example.store.bean.User;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.network.Network_util;
import com.example.store.network.UploadThread;
import com.example.store.server.Server;

public class HomeActivity extends Activity {
	// activity request code

	private DrawerLayout drawerlayout;

	private TextView textview_user_name, textView_signature;
	private ImageView imageView_photo;

	private Button home_but_login, home_but_logout, home_but_regedit;

	private ListView lv_user;// 左侧栏列表
	private ArrayList<String> lv_user_data;// 左侧栏列表数据
	private ArrayAdapter<String> lv_user_adapter;// 左侧栏列表适配器
	private ArrayList<String> lv_user_data_login;// 左侧栏列表已登入数据
	private ArrayList<String> lv_user_data_logout;// 左侧栏列表未登入数据

	private TextView weight_all, inpay_all, outpay_all, in_or_out_pay;
	private Button but_water, but_store, but_home, but_seting, but_about;
	private InStoreDaoInf dao;

	private SharedPreferences sPreferences;
	private User user;
	private Handler handler;
	private Server server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);
		dao = new InStoreDaoInf(this);
		server = new Server(this);
		// 初实化
		init();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ArrayList<Object> list = dao.getList(Summary.class);
		if (list != null) {
			for (Object object : list) {
				Summary Summary = (Summary) object;
				weight_all.setText("共有吨数：" + Summary.getWeight_all());
				inpay_all.setText(" 总付款：" + Summary.getInpay_all());
				outpay_all.setText(" 总收款：" + Summary.getOutpay_all());
				in_or_out_pay.setText("盈亏：" + Summary.getIn_or_out_pay());
			}
		} else {
			System.out.println("无数据 ~~~~~~~~~~~~~~~~~~~~~");
		}
	}

	// 初实化
	private void init() {
		// TODO Auto-generated method stub

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == CodeId.MessageID.GET_PHOTO_ONE_OK) {
					HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
					if (map != null) {
						imageView_photo.setImageBitmap((Bitmap) map
								.get("bitmap"));
						String string = sPreferences.getString("user_json", "");
						if (!"".equals(string)) {
							try {
								JSONObject object = new JSONObject(string);
								object.put("photo_image", map.get("url"));
								Editor editor = sPreferences.edit();
								editor.putString("user_json", object.toString());
								editor.commit();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						

					}

				}
				if (msg.what == CodeId.MessageID.LOGIN_USER_PASS) {
					String user_json_String = msg.obj.toString();
					loginUserPass(user_json_String);

				}
				if(msg.what==CodeId.MessageID.LOGIN_USER_FAIL)
				{
					new AlertDialog.Builder(HomeActivity.this).setCancelable(false).setTitle("提示！")
					.setMessage(msg.obj.toString()).setNeutralButton("确定", null)
					.create().show();
				}
				if(msg.what==CodeId.MessageID.UP_DATA_FILE_IS_OK)
				{
					Toast.makeText(HomeActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
				}
				if(msg.what==CodeId.MessageID.UN_LINE_OK)
				{
					home_but_logout.setEnabled(true);
					unLineOkDoing();
					Toast.makeText(HomeActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
				}
				if(msg.what==CodeId.MessageID.UN_LINE_FAIL)
				{
					if(!HomeActivity.this.isFinishing())
					{
						home_but_logout.setEnabled(true);
						new AlertDialog.Builder(HomeActivity.this).setCancelable(false).setTitle("提示！")
						.setMessage(msg.obj.toString()).setNeutralButton("确定", null)
						.create().show();
					}
				}
				if(msg.what==CodeId.MessageID.UPTABLE_OK)
				{
					server.cancellationUpFlag();
					Server.setDel_list(null);
					new AlertDialog.Builder(HomeActivity.this).setCancelable(false).setTitle("提示！")
					.setMessage(msg.obj.toString()).setNeutralButton("确定", null)
					.create().show();
				}
				if(msg.what==CodeId.MessageID.UPTABLE_FAIL)
				{
					new AlertDialog.Builder(HomeActivity.this).setCancelable(false).setTitle("提示！")
					.setMessage(msg.obj.toString()).setNeutralButton("确定", null)
					.create().show();
				}
			}

		};

		weight_all = (TextView) findViewById(R.id.textView_weight_all);
		inpay_all = (TextView) findViewById(R.id.textView_inpay_all);
		outpay_all = (TextView) findViewById(R.id.textView_outpay_all);
		in_or_out_pay = (TextView) findViewById(R.id.textView_in_or_out_pay);

		textview_user_name = (TextView) findViewById(R.id.textview_user_name);
		textView_signature = (TextView) findViewById(R.id.textView_signature);

		imageView_photo = (ImageView) findViewById(R.id.imageView_photo);
		lv_user_data = new ArrayList<String>();
		lv_user = (ListView) findViewById(R.id.lv_user);

		lv_user_data_login = new ArrayList<String>();
		lv_user_data_login.add("教程指示");
		lv_user_data_login.add("将数据存到sdcard");
		lv_user_data_login.add("上传数据到服务器");
		lv_user_data_login.add("下载数据到本地");
		lv_user_data_logout = new ArrayList<String>();
		lv_user_data_logout.add("教程指示");
		lv_user_data_logout.add("将数据存到sdcard");

		lv_user_adapter = new ArrayAdapter<String>(HomeActivity.this,
				android.R.layout.simple_expandable_list_item_1, lv_user_data);
		lv_user.setAdapter(lv_user_adapter);

		lv_user.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String valuesString = lv_user_data.get(position);
				if ("教程指示".equals(valuesString)) {
					startActivity(new Intent(HomeActivity.this,
							WelcomeAty.class));
				}else if ("将数据存到sdcard".equals(valuesString)) {
					
					new AlertDialog.Builder(HomeActivity.this).setCancelable(false).setTitle("害羞。")
					.setMessage(server.dataToSdcard()==true?"数据转移并保存成功！":"数据转移失败！").setNeutralButton("确定", null)
					.create().show();
				}else if("上传数据到服务器".equals(valuesString)){
					
					System.out.println("上传数据到服务器~~~~");
					server.dataToServer(handler);
					
				}else if("下载数据到本地".equals(valuesString))
				{
					
				}
				else {
					new AlertDialog.Builder(HomeActivity.this).setCancelable(false).setTitle("害羞。")
					.setMessage("不好意思，这个功能我还没学会。我会加油的，你在等等:) 。").setNeutralButton("确定", null)
					.create().show();
				}

			}
		});

		but_water = (Button) findViewById(R.id.but_water);
		but_store = (Button) findViewById(R.id.but_store);
		but_home = (Button) findViewById(R.id.but_home);
		but_seting = (Button) findViewById(R.id.but_seting);
		but_about = (Button) findViewById(R.id.but_about);

		home_but_login = (Button) findViewById(R.id.home_but_login);
		home_but_logout = (Button) findViewById(R.id.home_but_logout);
		home_but_regedit = (Button) findViewById(R.id.home_but_regedit);
		
		home_but_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, LoginAty.class);
				startActivityForResult(intent, CodeId.StartActivityID.LOGIN);

			}
		});
		home_but_regedit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(HomeActivity.this, RegeditAty.class),CodeId.StartActivityID.USER_REGEIDT);
			}
		});
		home_but_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setEnabled(false);
				sPreferences= HomeActivity.this.getSharedPreferences("user", MODE_APPEND);
				String user_json = sPreferences.getString("user_json", "");
				server.unLine(user_json,handler);
				
			}
		});

		OnClickListener listener = new OnClickListener();
		but_water.setOnClickListener(listener);
		but_store.setOnClickListener(listener);
		but_home.setOnClickListener(listener);
		but_seting.setOnClickListener(listener);
		but_about.setOnClickListener(listener);

		// 读取文件参数，并设置
		sPreferences = this.getSharedPreferences("user", MODE_APPEND);
		boolean auto_login = sPreferences.getBoolean("auto_login", false);
		if (auto_login) {
			String user_json = sPreferences.getString("user_json", "");

			Network_util.login_user(handler, user_json);

		} else {
			lv_user_data.clear();
			lv_user_data.addAll(lv_user_data_logout);
			lv_user_adapter.notifyDataSetChanged();
			home_but_login.setVisibility(View.VISIBLE);
			home_but_regedit.setVisibility(View.VISIBLE);
			home_but_logout.setVisibility(View.GONE);
		}
	}

	// 启动进出货界面
	public void load_aty_inout(View view) {
		startActivity(new Intent(this, InAndOutAty.class));
	}

	// 点击类
	class OnClickListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.but_water) {
				startActivity(new Intent(HomeActivity.this, WaterAty.class));
			}
			if (v.getId() == R.id.but_store) {
				startActivity(new Intent(HomeActivity.this, StoreAty.class));
			}
			if (v.getId() == R.id.but_seting) {
				startActivity(new Intent(HomeActivity.this, KindAty.class));
			}
			if (v.getId() == R.id.but_about) {
			}
		}

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case CodeId.StartActivityID.USER_REGEIDT:
			if (resultCode == Activity.RESULT_OK) {
				try {
					JSONObject userJsonObject = new JSONObject(
							data.getStringExtra("user_json"));
					System.out.println("homeActy  返回的注册结果："+userJsonObject.toString());
					textview_user_name.setText(userJsonObject
							.getString("user_name"));
					textView_signature.setText(userJsonObject
							.getString("signature"));
					userJsonObject.put("user_psw", "");

					 //保存到SharedPreferences
					 Editor editor = sPreferences.edit();
					 editor.putString("user_json", userJsonObject.toString());
					 editor.commit();
					home_but_login.setVisibility(View.GONE);
					home_but_regedit.setVisibility(View.GONE);
					home_but_logout.setVisibility(View.VISIBLE);
					loginUserPass(userJsonObject.toString());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case CodeId.StartActivityID.SET_USER_PARAMETER:
			if (resultCode == Activity.RESULT_OK) {
				String user_json = data.getStringExtra("user_json");
				if (!user_json.equals("")) {
					try {
						JSONObject userJsonObject = new JSONObject(user_json);
						if (userJsonObject.has("user_name")) {
							String user_name = userJsonObject
									.getString("user_name");
							if (!user_name.equals("")) {
								textview_user_name.setText(user_name);
							}
						}
						if (userJsonObject.has("signature")) {
							String signature = userJsonObject
									.getString("signature");
							if (!signature.equals("")) {
								textView_signature.setText(signature);
							}
						}
						if (userJsonObject.has("photo_image")) {
							String signature = userJsonObject
									.getString("photo_image");
							if (!signature.equals("") && !signature.equals("0")) {
								Network_util.getPhotoBuyOne(handler, signature);
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			break;
			case CodeId.StartActivityID.LOGIN:
				if(resultCode==Activity.RESULT_OK)
				{
					String user_json = data.getStringExtra("user_json");
					System.out.println("调用登入ATY返回的结果："+user_json);
					loginUserPass(user_json);
				}
		default:
			break;
		}
	}

	/**
	 * 设置头像按扭事件
	 * 
	 * @param view
	 */
	public void setPhoto(View view) {

		
//		new AlertDialog.Builder(HomeActivity.this).setCancelable(false).setTitle("害羞。")
//		.setMessage("不好意思，这个功能我还没学会。我会加油的，你在等等    :) 。").setNeutralButton("确定", null)
//		.create().show();
		
		String string = sPreferences.getString("user_json", "");
		if (sPreferences.getBoolean("online", false)) {
			// 启动用户参数界面
			Intent intent = new Intent();
			intent.putExtra("user_json", string);
			intent.setClass(this, UserParameterCard.class);
			startActivityForResult(intent, CodeId.StartActivityID.SET_USER_PARAMETER);
		} else {
			new AlertDialog.Builder(this)
					.setTitle("提示！")
					.setMessage("您还未登入，是否现在进行登入？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									startActivityForResult(
											new Intent(HomeActivity.this,
													LoginAty.class),
													CodeId.StartActivityID.LOGIN);

								}
							})
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									return;

								}
							}).create().show();
		}

	}

	/**
	 * 用户登入验证成功后的操作
	 * @param user_json_String 验证成功的用户
	 */
	public void loginUserPass(String user_json_String) {
		if (user_json_String != null
				&& !"".equals(user_json_String)) {
			try {
				JSONObject userJsonObject = new JSONObject(
						user_json_String);
				if (userJsonObject.has("user_name")) {
					String user_name = userJsonObject
							.getString("user_name");
					if (!user_name.equals("")) {
						textview_user_name.setText(user_name);
					}
				}
				if (userJsonObject.has("signature")) {
					String signature = userJsonObject
							.getString("signature");
					if (!signature.equals("")) {
						textView_signature.setText(signature);
					}
				}
				if (userJsonObject.has("photoImage")) {
					String signature = userJsonObject
							.getString("photoImage");
					if (!signature.equals("")
							&& !signature.equals("0")) {
						Network_util.getPhotoBuyOne(handler,
								signature);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lv_user_data.clear();
			lv_user_data.addAll(lv_user_data_login);
			lv_user_adapter.notifyDataSetChanged();
			home_but_login.setVisibility(View.GONE);
			home_but_regedit.setVisibility(View.GONE);
			home_but_logout.setVisibility(View.VISIBLE);
			
			sPreferences = this.getSharedPreferences("user", MODE_APPEND);
			Editor editor = sPreferences.edit();
			editor.putBoolean("online", true);
			editor.commit();
			Toast.makeText(this, "登入成功！", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("提示！").setCancelable(false).setMessage("您确定要退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sPreferences= HomeActivity.this.getSharedPreferences("user", MODE_APPEND);
				String user_json = sPreferences.getString("user_json", "");
				server.unLine(user_json,handler);
//				sPreferences = HomeActivity.this.getSharedPreferences("user", MODE_APPEND);
//				Editor editor = sPreferences.edit();
//				editor.putBoolean("online", false);
//				editor.commit();
				finish();
				
			}
		}).setNegativeButton("取消", null).create().show();
	}
	/**
	 * 下线成功后的布局
	 */
	public boolean unLineOkDoing() {
		imageView_photo.setImageResource(R.drawable.defaultl);
		textview_user_name.setText("???");
		textView_signature.setText("???");
		
		Editor editor = sPreferences.edit();
		editor.putBoolean("online", false);
		editor.commit();
		lv_user_data.clear();
		lv_user_data.addAll(lv_user_data_logout);
		lv_user_adapter.notifyDataSetChanged();
		home_but_login.setVisibility(View.VISIBLE);
		home_but_regedit.setVisibility(View.VISIBLE);
		home_but_logout.setVisibility(View.GONE);
		return true;
	}

}
