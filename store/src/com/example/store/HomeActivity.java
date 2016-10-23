package com.example.store;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.store.bean.Summary;
import com.example.store.bean.User;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.network.Network_util;
import com.example.store.server.Server;
import com.example.view_component.viewComponent;

public class HomeActivity extends Activity {
	// activity request code

	private DrawerLayout drawerlayout;

	private TextView textview_user_name, textView_signature,textview_user_id;
	private ImageView imageView_photo;

	private Button home_but_login, home_but_logout, home_but_regedit;

	private ListView lv_user;// ������б�
	private ArrayList<String> lv_user_data;// ������б�����
	private ArrayAdapter<String> lv_user_adapter;// ������б�������
	private ArrayList<String> lv_user_data_login;// ������б��ѵ�������
	private ArrayList<String> lv_user_data_logout;// ������б�δ��������

	private TextView weight_all, inpay_all, outpay_all, in_or_out_pay;
	private Button but_water, but_store, but_home, but_seting, but_about;
	private InStoreDaoInf dao;

	private SharedPreferences sPreferences;
	private User user;
	private Handler handler;
	private Server server;
	private Dialog waitDialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);
		dao = new InStoreDaoInf(this);
		server = new Server(this);
		// ��ʵ��
		init();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setMainView();
	}

	public void setMainView() {
		ArrayList<Object> list = dao.getList(Summary.class);
		if (list != null) {
			for (Object object : list) {
				Summary Summary = (Summary) object;
				weight_all.setText("���ж�����" + Summary.getWeight_all());
				inpay_all.setText(" �ܸ��" + Summary.getInpay_all());
				outpay_all.setText(" ���տ" + Summary.getOutpay_all());
				in_or_out_pay.setText("ӯ����" + Summary.getIn_or_out_pay());
			}
		} else {
			System.out.println("������ ~~~~~~~~~~~~~~~~~~~~~");
		}
	}

	// ��ʵ��
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
					try {
						loginUserPass(new JSONObject(user_json_String));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (msg.what == CodeId.MessageID.LOGIN_USER_FAIL) {
					waitDialog.dismiss();
					new AlertDialog.Builder(HomeActivity.this)
							.setCancelable(false).setTitle("��ʾ��")
							.setMessage(msg.obj.toString())
							.setNeutralButton("ȷ��", null).create().show();
				}
				if (msg.what == CodeId.MessageID.UP_DATA_FILE_IS_OK) {
					Toast.makeText(HomeActivity.this, msg.obj.toString(),
							Toast.LENGTH_LONG).show();
				}
				if (msg.what == CodeId.MessageID.UN_LINE_OK) {
					home_but_logout.setEnabled(true);
					unLineOkDoing();
					Toast.makeText(HomeActivity.this, msg.obj.toString(),
							Toast.LENGTH_LONG).show();
				}
				if (msg.what == CodeId.MessageID.UN_LINE_FAIL) {
					if (!HomeActivity.this.isFinishing()) {
						home_but_logout.setEnabled(true);
						new AlertDialog.Builder(HomeActivity.this)
								.setCancelable(false).setTitle("��ʾ��")
								.setMessage(msg.obj.toString())
								.setNeutralButton("ȷ��", null).create().show();
					}
				}
				if (msg.what == CodeId.MessageID.UPTABLE_OK) {
					server.cancellationUpFlag();
					Server.setDel_list(null);
					waitDialog.dismiss();
					new AlertDialog.Builder(HomeActivity.this)
							.setCancelable(false).setTitle("��ʾ��")
							.setMessage(msg.obj.toString())
							.setNeutralButton("ȷ��", null).create().show();
				}
				if (msg.what == CodeId.MessageID.UPTABLE_FAIL) {
					waitDialog.dismiss();
					new AlertDialog.Builder(HomeActivity.this)
							.setCancelable(false).setTitle("��ʾ��")
							.setMessage(msg.obj.toString())
							.setNeutralButton("ȷ��", null).create().show();
				}
				//��ȡ���������ݿ����� �ɹ���Jsonarray����
				if (msg.what == CodeId.MessageID.GET_ALL_DB_OK) {
					addDataToLocal(msg.obj);
				}
				if (msg.what == CodeId.MessageID.GET_ALL_DB_FAIL) {
					waitDialog.dismiss();
					new AlertDialog.Builder(HomeActivity.this)
							.setCancelable(false).setTitle("��ʾ��")
							.setMessage(msg.obj.toString())
							.setNeutralButton("ȷ��", null).create().show();
				}
			}

		};

		weight_all = (TextView) findViewById(R.id.textView_weight_all);
		inpay_all = (TextView) findViewById(R.id.textView_inpay_all);
		outpay_all = (TextView) findViewById(R.id.textView_outpay_all);
		in_or_out_pay = (TextView) findViewById(R.id.textView_in_or_out_pay);

		textview_user_name = (TextView) findViewById(R.id.textview_user_name);
		textView_signature = (TextView) findViewById(R.id.textView_signature);
		textview_user_id = (TextView) findViewById(R.id.textview_user_id);

		imageView_photo = (ImageView) findViewById(R.id.imageView_photo);
		lv_user_data = new ArrayList<String>();
		lv_user = (ListView) findViewById(R.id.lv_user);

		lv_user_data_login = new ArrayList<String>();
		lv_user_data_login.add("�̳�ָʾ");
		lv_user_data_login.add("�����ݴ浽sdcard");
		lv_user_data_login.add("�ϴ����ݵ�������");
		lv_user_data_login.add("�������ݵ�����");
		lv_user_data_logout = new ArrayList<String>();
		lv_user_data_logout.add("�̳�ָʾ");
		lv_user_data_logout.add("�����ݴ浽sdcard");

		lv_user_adapter = new ArrayAdapter<String>(HomeActivity.this,
				android.R.layout.simple_expandable_list_item_1, lv_user_data);
		lv_user.setAdapter(lv_user_adapter);

		lv_user.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String valuesString = lv_user_data.get(position);
				if ("�̳�ָʾ".equals(valuesString)) {
					startActivity(new Intent(HomeActivity.this,
							WelcomeAty.class));
				} else if ("�����ݴ浽sdcard".equals(valuesString)) {

					new AlertDialog.Builder(HomeActivity.this)
							.setCancelable(false)
							.setTitle("���ߡ�")
							.setMessage(
									server.dataToSdcard() == true ? "����ת�Ʋ�����ɹ���"
											: "����ת��ʧ�ܣ�")
							.setNeutralButton("ȷ��", null).create().show();
				} else if ("�ϴ����ݵ�������".equals(valuesString)) {

					final EditText serverurlEditText = new EditText(HomeActivity.this);
					serverurlEditText.setText(CodeId.getUrl());
					new AlertDialog.Builder(HomeActivity.this)
					.setCancelable(false).setTitle("��Ƥ�ķ�������")
					.setMessage("�����ڰ����ϴ������Ƿ�������֪����ȥ�ˣ�ֻҪ������������ģ���һ��������������").setView(serverurlEditText)
					.setNeutralButton("ȡ��", null).setPositiveButton("���㣡", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							CodeId.setUrl(serverurlEditText.getText().toString());
							waitDialog=viewComponent.waitServerGetSomeThing(HomeActivity.this,"���Եȡ���","�����ϴ����ݵ������������Ժ򡣡���");
							server.dataToServer(handler);
						}
					}).create().show();
					

				} else if ("�������ݵ�����".equals(valuesString)) {
					//��ʾ�ȴ�������
					final EditText serverurlEditText = new EditText(HomeActivity.this);
					serverurlEditText.setText(CodeId.getUrl());
					new AlertDialog.Builder(HomeActivity.this)
					.setCancelable(false).setTitle("��Ƥ�ķ�������")
					.setMessage("����׼���Ϸ������Ǹ��������ݣ����Ƿ�������֪����ȥ�ˡ�ֻҪ������������ģ���һ��������������").setView(serverurlEditText)
					.setNeutralButton("ȡ��", null).setPositiveButton("���㣡", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							CodeId.setUrl(serverurlEditText.getText().toString());
							waitDialog=viewComponent.waitServerGetSomeThing(HomeActivity.this,"���Եȡ�����","���ڴӷ������϶�ȡ���ݿ���Ϣ�����Ժ򡣡���");
							Network_util.getAllData(handler,textview_user_id.getText().toString());
						}
					}).create().show();
					

				} else {
					new AlertDialog.Builder(HomeActivity.this)
							.setCancelable(false).setTitle("���ߡ�")
							.setMessage("������˼����������һ�ûѧ�ᡣ�һ���͵ģ����ڵȵ�:) ��")
							.setNeutralButton("ȷ��", null).create().show();
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
				
				final EditText serverurlEditText = new EditText(HomeActivity.this);
				serverurlEditText.setText(CodeId.getUrl());
				new AlertDialog.Builder(HomeActivity.this)
				.setCancelable(false).setTitle("��Ƥ�ķ�������")
				.setMessage("������˼�����������֪����ȥ�ˡ�ֻ��������ҷ��������ģ��ſ��Խ��е���Ŷ��").setView(serverurlEditText)
				.setNeutralButton("ȡ��", null).setPositiveButton("���㣡", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						CodeId.setUrl(serverurlEditText.getText().toString());
						Intent intent = new Intent();
						intent.setClass(HomeActivity.this, LoginAty.class);
						startActivityForResult(intent, CodeId.StartActivityID.LOGIN);
					}
				}).create().show();
				
				

			}
		});
		home_but_regedit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final EditText serverurlEditText = new EditText(HomeActivity.this);
				serverurlEditText.setText(CodeId.getUrl());
				new AlertDialog.Builder(HomeActivity.this)
				.setCancelable(false).setTitle("��Ƥ�ķ�������")
				.setMessage("������˼�����������֪����ȥ�ˡ�ֻ��������ҷ��������ģ��ſ��Խ���ע��Ŷ��").setView(serverurlEditText)
				.setNeutralButton("ȡ��", null).setPositiveButton("���㣡", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						CodeId.setUrl(serverurlEditText.getText().toString());
						startActivityForResult(new Intent(HomeActivity.this,
								RegeditAty.class), CodeId.StartActivityID.USER_REGEIDT);
					}
				}).create().show();
				
				
			}
		});
		home_but_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setEnabled(false);
				sPreferences = HomeActivity.this.getSharedPreferences("user",
						MODE_APPEND);
				String user_json = sPreferences.getString("user_json", "");
				server.unLine(user_json, handler);

			}
		});

		OnClickListener listener = new OnClickListener();
		but_water.setOnClickListener(listener);
		but_store.setOnClickListener(listener);
		but_home.setOnClickListener(listener);
		but_seting.setOnClickListener(listener);
		but_about.setOnClickListener(listener);

		// ��ȡ�ļ�������������
		sPreferences = this.getSharedPreferences("user", MODE_APPEND);
		boolean auto_login = sPreferences.getBoolean("auto_login", false);
		if (auto_login) {
			
			final EditText serverurlEditText = new EditText(HomeActivity.this);
			serverurlEditText.setText(CodeId.getUrl());
			new AlertDialog.Builder(HomeActivity.this)
			.setCancelable(false).setTitle("��Ƥ�ķ�������")
			.setMessage("������Ϊ������Զ������У��ɷ�������֪������ȥ�ˣ���֪����").setView(serverurlEditText)
			.setNeutralButton("ȡ��", null).setPositiveButton("���㣡", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					CodeId.setUrl(serverurlEditText.getText().toString());
					waitDialog=viewComponent.waitServerGetSomeThing(HomeActivity.this,"���Ե�","����Ϊ�����룬���Ժ󡣡���");
					String user_json = sPreferences.getString("user_json", "");
					Network_util.login_user(handler, user_json);
				}
			}).create().show();
			
			

		} else {
			lv_user_data.clear();
			lv_user_data.addAll(lv_user_data_logout);
			lv_user_adapter.notifyDataSetChanged();
			home_but_login.setVisibility(View.VISIBLE);
			home_but_regedit.setVisibility(View.VISIBLE);
			home_but_logout.setVisibility(View.GONE);
		}
	}

	protected void addDataToLocal(Object obj) {
		// TODO Auto-generated method stub
		if(server.addDataToLocal(obj))
		{	
			waitDialog.dismiss();
			setMainView();
			Toast.makeText(HomeActivity.this, "���ݸ��³ɹ���", Toast.LENGTH_LONG).show();
		}
		
	}

	// ��������������
	public void load_aty_inout(View view) {
		startActivity(new Intent(this, InAndOutAty.class));
	}

	// �����
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
					System.out.println("homeActy  ���ص�ע������"
							+ userJsonObject.toString());
					textview_user_id.setText(userJsonObject
							.getInt("id")+"");
					textview_user_name.setText(userJsonObject
							.getString("user_name"));
					textView_signature.setText(userJsonObject
							.getString("signature"));
					userJsonObject.put("user_psw", "");

					// ���浽SharedPreferences
					Editor editor = sPreferences.edit();
					editor.putString("user_json", userJsonObject.toString());
					editor.commit();
					home_but_login.setVisibility(View.GONE);
					home_but_regedit.setVisibility(View.GONE);
					home_but_logout.setVisibility(View.VISIBLE);
					loginUserPass(userJsonObject);

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
			if (resultCode == Activity.RESULT_OK) {
				boolean is_same = data.getBooleanExtra("is_same", true);
				String user_jsonstring = data.getStringExtra("user_json");
				try {
					if (!"".equals(user_jsonstring)) {
						final JSONObject userjson = new JSONObject(user_jsonstring);
						if (is_same) {
							// ��ȡ�������ϸ��µļ�¼
							// server.getServerNewDataToLoard(handler);
						} else {
							// ��ȡ�������ϵ����м�¼������
							//��ʾ�ȴ�������
							waitDialog=viewComponent.waitServerGetSomeThing(HomeActivity.this,"���Եȡ�����","���ڴӷ������϶�ȡ���ݿ���Ϣ�����Ժ򡣡���");
							try {
								int db_id = userjson.getInt("id");
								System.out.println("׼����ȡ���û����ݿ��׺�ǣ�"+db_id);
								server.getServerAllDataToLoard(handler,userjson.getInt("id")+"");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						System.out.println("���õ���ATY���صĽ����" + userjson);
						loginUserPass(userjson);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		default:
			break;
		}
	}
	

	/**
	 * ����ͷ��Ť�¼�
	 * 
	 * @param view
	 */
	public void setPhoto(View view) {

		// new
		// AlertDialog.Builder(HomeActivity.this).setCancelable(false).setTitle("���ߡ�")
		// .setMessage("������˼����������һ�ûѧ�ᡣ�һ���͵ģ����ڵȵ�    :) ��").setNeutralButton("ȷ��",
		// null)
		// .create().show();

		String string = sPreferences.getString("user_json", "");
		if (sPreferences.getBoolean("online", false)) {
			// �����û���������
			Intent intent = new Intent();
			intent.putExtra("user_json", string);
			intent.setClass(this, UserParameterCard.class);
			startActivityForResult(intent,
					CodeId.StartActivityID.SET_USER_PARAMETER);
		} else {
			new AlertDialog.Builder(this)
					.setTitle("��ʾ��")
					.setMessage("����δ���룬�Ƿ����ڽ��е��룿")
					.setPositiveButton("��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									
									final EditText serverurlEditText = new EditText(HomeActivity.this);
									serverurlEditText.setText(CodeId.getUrl());
									new AlertDialog.Builder(HomeActivity.this)
									.setCancelable(false).setTitle("��Ƥ�ķ�������")
									.setMessage("������˼�����������֪����ȥ�ˡ�ֻ��������ҷ��������ģ��ſ��Խ��е���Ŷ��").setView(serverurlEditText)
									.setNeutralButton("ȡ��", null).setPositiveButton("���㣡", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											CodeId.setUrl(serverurlEditText.getText().toString());
											startActivityForResult(new Intent(HomeActivity.this, LoginAty.class),CodeId.StartActivityID.LOGIN);
											
										}
									}).create().show();
									
									

								}
							})
					.setNegativeButton("��",
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
	 * �û�������֤�ɹ���Ĳ���
	 * 
	 * @param user_json_String
	 *            ��֤�ɹ����û�
	 */
	public void loginUserPass(JSONObject user_json) {
			try {
				if (user_json.has("id")) {
					String user_id = user_json.getInt("id")+"";
					if (!user_id.equals("")) {
						textview_user_id.setText(user_id);
					}
				}
				if (user_json.has("user_name")) {
					String user_name = user_json.getString("user_name");
					if (!user_name.equals("")) {
						textview_user_name.setText(user_name);
					}
				}
				if (user_json.has("signature")) {
					String signature = user_json.getString("signature");
					if (!signature.equals("")) {
						textView_signature.setText(signature);
					}
				}
				if (user_json.has("photoImage")) {
					String signature = user_json.getString("photoImage");
					if (!signature.equals("") && !signature.equals("0")) {
						Network_util.getPhotoBuyOne(handler, signature);
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
			if(waitDialog!=null)
			{
				waitDialog.dismiss();
			}
			Toast.makeText(this, "����ɹ���", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("��ʾ��").setCancelable(false)
				.setMessage("��ȷ��Ҫ�˳���")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sPreferences = HomeActivity.this.getSharedPreferences(
								"user", MODE_APPEND);
						String user_json = sPreferences.getString("user_json",
								"");
						server.unLine(user_json, handler);
						// sPreferences =
						// HomeActivity.this.getSharedPreferences("user",
						// MODE_APPEND);
						// Editor editor = sPreferences.edit();
						// editor.putBoolean("online", false);
						// editor.commit();
						finish();

					}
				}).setNegativeButton("ȡ��", null).create().show();
	}

	/**
	 * ���߳ɹ���Ĳ���
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
