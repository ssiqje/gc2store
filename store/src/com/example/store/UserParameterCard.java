package com.example.store;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.store.network.Network_util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UserParameterCard extends Activity {
	private ImageView imageview_suer_parameter_card_user_photo_image;
	private LinearLayout layout_user_parameter_card_for_photo;
	private GridView gridView_suer_parameter_card_user_photo;
	private RelativeLayout layout_user_parameter_card_for_other;
	private EditText editText_suer_parameter_card_user_name,
			editText_suer_parameter_card_user_signature;

	private Button but_user_parameter_card_photo_list_confirm,
			but_user_parameter_card_photo_list_cancel,
			but_user_parameter_card_confirm, but_user_parameter_card_cancel;

	private Handler handler;
	private JSONObject userJsonObject;
	private String photo_url;
	private String user_name;
	private String user_signature;

	private SharedPreferences sPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_parameter_card);
		String s = getIntent().getStringExtra("user_json");
		if (s != null && !"".equals(s)) {
			try {
				userJsonObject = new JSONObject(s);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == CodeId.MessageID.GET_PHOTO_ONE_OK) {
					HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
					if (map != null) {
						imageview_suer_parameter_card_user_photo_image
								.setImageBitmap((Bitmap) map.get("bitmap"));
						photo_url = map.get("url").toString();

					}
				}
				if (msg.what == CodeId.MessageID.GET_PHOTO_LIST_OK) {
					final ArrayList<HashMap<String, Object>> photo_list_data = (ArrayList<HashMap<String, Object>>) msg.obj;
					if (photo_list_data != null && !photo_list_data.isEmpty()) {

						layout_user_parameter_card_for_other
								.setVisibility(View.GONE);
						layout_user_parameter_card_for_photo
								.setVisibility(View.VISIBLE);

						gridView_suer_parameter_card_user_photo
								.setAdapter(new PhotoListAdapter(
										UserParameterCard.this, photo_list_data));
						gridView_suer_parameter_card_user_photo
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										// TODO Auto-generated method stub
										imageview_suer_parameter_card_user_photo_image
												.setImageBitmap((Bitmap) (photo_list_data
														.get(position)
														.get("bitmap")));
										photo_url = photo_list_data
												.get(position).get("url")
												.toString();

									}
								});
					} else {
						new AlertDialog.Builder(UserParameterCard.this)
								.setTitle("天呐")
								.setMessage("不对起！服务器被谁黑了,头像数据暂时找不到了。熊孩子真可恶！")
								.setNeutralButton("可恶的熊孩子", null).create()
								.show();
					}
				}
				if (msg.what == CodeId.MessageID.UP_USER_DATA_OK) {
					sPreferences = UserParameterCard.this.getSharedPreferences(
							"user", MODE_APPEND);
					Editor editor = sPreferences.edit();

					editor.putString("user_json", userJsonObject.toString());
					editor.commit();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							UserParameterCard.this);

					builder.setCancelable(false).setTitle("提示")
							.setMessage(msg.obj.toString())
							.setNeutralButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent intent = new Intent();
									intent.putExtra("user_json",
											userJsonObject.toString());
									setResult(Activity.RESULT_OK, intent);
									finish();
								}
							}).create().show();

				}
			}

		};
		imageview_suer_parameter_card_user_photo_image = (ImageView) findViewById(R.id.imageview_suer_parameter_card_user_photo_image);
		editText_suer_parameter_card_user_name = (EditText) findViewById(R.id.editText_suer_parameter_card_user_name);
		editText_suer_parameter_card_user_signature = (EditText) findViewById(R.id.editText_suer_parameter_card_user_signature);
		layout_user_parameter_card_for_photo = (LinearLayout) findViewById(R.id.layout_user_parameter_card_for_photo);
		layout_user_parameter_card_for_other = (RelativeLayout) findViewById(R.id.layout_user_parameter_card_for_other);
		gridView_suer_parameter_card_user_photo = (GridView) findViewById(R.id.gridView_suer_parameter_card_user_photo);
		// 设置头像点击事件
		imageview_suer_parameter_card_user_photo_image.setClickable(true);
		imageview_suer_parameter_card_user_photo_image
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Network_util.getPhotoList(handler);
						v.setClickable(false);

					}
				});

		but_user_parameter_card_cancel = (Button) findViewById(R.id.but_user_parameter_cardt_cancel);
		but_user_parameter_card_confirm = (Button) findViewById(R.id.but_user_parameter_card_confirm);
		but_user_parameter_card_photo_list_cancel = (Button) findViewById(R.id.but_user_parameter_card_photo_list_cancel);
		but_user_parameter_card_photo_list_confirm = (Button) findViewById(R.id.but_user_parameter_card_photo_list_confirm);

		// 设置确定取消按按事件

		but_user_parameter_card_photo_list_cancel
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						layout_user_parameter_card_for_other
								.setVisibility(View.VISIBLE);
						layout_user_parameter_card_for_photo
								.setVisibility(View.GONE);
						imageview_suer_parameter_card_user_photo_image
								.setClickable(true);
					}
				});

		but_user_parameter_card_photo_list_confirm
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						layout_user_parameter_card_for_other
								.setVisibility(View.VISIBLE);
						layout_user_parameter_card_for_photo
								.setVisibility(View.GONE);
						imageview_suer_parameter_card_user_photo_image
								.setClickable(true);
					}
				});

		but_user_parameter_card_cancel
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});

		but_user_parameter_card_confirm
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						user_name = editText_suer_parameter_card_user_name
								.getText().toString();
						user_signature = editText_suer_parameter_card_user_signature
								.getText().toString();
						try {
							userJsonObject.put("photo_image", photo_url);
							userJsonObject.put("user_name", user_name);
							userJsonObject.put("signature", user_signature);

							Network_util.upUser(handler, userJsonObject);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

		// 界面内容初实化
		if (userJsonObject != null) {
			try {
				if (userJsonObject.has("photo_image")) {
					photo_url = userJsonObject.getString("photo_image");
					if (!"0".equals(photo_url)) {
						Network_util.getPhotoBuyOne(handler, photo_url);
					}
				}
				// 设置用户名
				if (userJsonObject.has("user_name")) {
					user_name = userJsonObject.getString("user_name");
					editText_suer_parameter_card_user_name.setText(user_name);
				}
				// 设置用户签名
				if (userJsonObject.has("signature")) {
					user_signature = userJsonObject.getString("signature");
					editText_suer_parameter_card_user_signature
							.setText(user_signature);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 头像列表适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class PhotoListAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> list;
		private Context context;

		public PhotoListAdapter(Context context,
				ArrayList<HashMap<String, Object>> photList) {
			this.list = photList;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView != null) {
				ViewHot viewHot = (ViewHot) convertView.getTag();
				viewHot.getImageView().setImageBitmap(
						(Bitmap) list.get(position).get("bitmap"));
				viewHot.getTitle().setText(
						list.get(position).get("title").toString());
				convertView = viewHot.getView();
				convertView.setTag(viewHot);

			} else {
				ViewHot viewHot = new ViewHot(context);
				viewHot.getImageView().setImageBitmap(
						(Bitmap) list.get(position).get("bitmap"));
				viewHot.getTitle().setText(
						list.get(position).get("title").toString());
				convertView = viewHot.getView();
				convertView.setTag(viewHot);
			}
			return convertView;
		}

	}

	/**
	 * 列表子项类
	 * 
	 * @author Administrator
	 * 
	 */
	private class ViewHot {
		private View view;
		private ImageView imageView;
		private TextView title;

		public ViewHot(Context context) {
			this.view = LayoutInflater.from(context).inflate(
					R.layout.list_view_photo_item, null);
			imageView = (ImageView) view
					.findViewById(R.id.imageView_photo_item_photo);
			title = (TextView) view
					.findViewById(R.id.textView_photo_item_title);
		}

		public View getView() {
			return view;
		}

		public ImageView getImageView() {
			return imageView;
		}

		public TextView getTitle() {
			return title;
		}

	}

}
