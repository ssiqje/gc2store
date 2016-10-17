package com.example.store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.example.store.bean.GcParameter;
import com.example.store.bean.In_store;
import com.example.store.bean.Out_store;
import com.example.store.bean.Store;
import com.example.store.bean.Summary;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.server.Server;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

public class InAndOutAty extends Activity {
	private TabHost tabHost;
	private Spinner inSpinner, outSpinner;
	private EditText in_weight_m, in_gc_long, in_inpay_m, in_count,
			out_weight_m, out_gc_long, out_outpay_m, out_count;
	private ListView lvin, lvout;
	private List<HashMap<String, String>> inList, outList;
	private SimpleAdapter inAdapter, outAdapter;
	private InStoreDaoInf dao;
	private Server server;
	private String[] spinnerStrings;
	private ArrayList<Object> list;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inout);
		
		handler = new Handler();
		tabHost = (TabHost) findViewById(R.id.tabhost);
		inSpinner = (Spinner) findViewById(R.id.in_spinner);
		outSpinner = (Spinner) findViewById(R.id.out_spinner);

		in_weight_m = (EditText) findViewById(R.id.in_weight_m);
		in_gc_long = (EditText) findViewById(R.id.in_gc_long);
		in_inpay_m = (EditText) findViewById(R.id.in_inpay_m);
		in_count = (EditText) findViewById(R.id.in_count);

		out_weight_m = (EditText) findViewById(R.id.out_weight_m);
		out_gc_long = (EditText) findViewById(R.id.out_gc_long);
		out_outpay_m = (EditText) findViewById(R.id.out_outpay_m);
		out_count = (EditText) findViewById(R.id.out_count);

		lvin = (ListView) findViewById(R.id.lvin);
		lvout = (ListView) findViewById(R.id.lvout);

		inList = new ArrayList<HashMap<String, String>>();
		outList = new ArrayList<HashMap<String, String>>();

		inAdapter = new SimpleAdapter(this, inList, R.layout.item_in_or_out,
				new String[] { "kind_id", "inpay_m", "weight", "allpay" },
				new int[] { R.id.item_in_or_out_kind_id,
						R.id.item_in_or_out_pay_m, R.id.item_in_or_out_weight,
						R.id.item_in_or_out_allpay });
		outAdapter = new SimpleAdapter(this, outList, R.layout.item_in_or_out,
				new String[] { "kind_id", "inpay_m", "weight", "allpay" },
				new int[] { R.id.item_in_or_out_kind_id,
						R.id.item_in_or_out_pay_m, R.id.item_in_or_out_weight,
						R.id.item_in_or_out_allpay });

		lvin.setAdapter(inAdapter);
		lvout.setAdapter(outAdapter);

		lvin.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stubn
				new AlertDialog.Builder(InAndOutAty.this).setTitle("提示！")
						.setMessage("您确定要删除此项吗？")
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (server.delItemFromInStore(inList.get(position))) {	
									inList.remove(position);
									inAdapter.notifyDataSetChanged();
									Toast.makeText(InAndOutAty.this, "删除成功！",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(InAndOutAty.this, "未能成功删除！",
											Toast.LENGTH_LONG).show();
								}
							}
						}).setNegativeButton("取消", null).create().show();

			}
		});
		lvout.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stubn
				new AlertDialog.Builder(InAndOutAty.this).setTitle("提示！")
						.setMessage("您确定要删除此项吗？")
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (server.delItemFromOutStore(outList.get(position))) {
									outList.remove(position);
									outAdapter.notifyDataSetChanged();
									Toast.makeText(InAndOutAty.this, "删除成功！",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(InAndOutAty.this, "未能成功删除！",
											Toast.LENGTH_LONG).show();
								}
							}
						}).setNegativeButton("取消", null).create().show();

			}
		});
		dao = new InStoreDaoInf(this);
		server = new Server(this);

		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("in").setIndicator("进货")
				.setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("out").setIndicator("出货")
				.setContent(R.id.tab2));

		list = dao.getList(GcParameter.class);

		if (list != null) {
			spinnerStrings = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				GcParameter gcParameter = (GcParameter) list.get(i);
				spinnerStrings[i] = gcParameter.getKind_id();

			}
			inSpinner.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, spinnerStrings));
			outSpinner.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, spinnerStrings));
			inSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					GcParameter gcParameter = (GcParameter) list.get(position);
					in_weight_m.setText(gcParameter.getWeight_m() + "");
					in_gc_long.setText(gcParameter.getGc_long() + "");
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});
			outSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					GcParameter gcParameter = (GcParameter) list.get(position);
					out_weight_m.setText(gcParameter.getWeight_m() + "");
					out_gc_long.setText(gcParameter.getGc_long() + "");
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});
		} else {
			new Builder(InAndOutAty.this).setTitle("提示！").setMessage("种类还未设置，是否现在进行设置！").setNegativeButton("否", null)
			.setPositiveButton("是", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startActivity(new Intent(InAndOutAty.this, KindAty.class));
					finish();
				}
			}).create().show();
		}

	}

	/**
	 * 进货按扭事件
	 * 进货 1，进货流水2，库存查寻插入3，总表，更改重量、更改总付款
	 * 
	 * @param view
	 */
	public void Button_in_item(View view) {
		final long id = System.currentTimeMillis();
		final String kind_id = (String) inSpinner.getSelectedItem();
		final String weight_m = in_weight_m.getText().toString();
		final String gc_long = in_gc_long.getText().toString();
		final String inpay_m = in_inpay_m.getText().toString();
		final String count = in_count.getText().toString();
		final String date = simpleDateFormat.format(new Date());
		if (!server.isNull(kind_id, weight_m, gc_long, inpay_m, count, date)) {
			new AlertDialog.Builder(InAndOutAty.this).setTitle("提示！").setMessage("数据不能为空！").setCancelable(false).setNeutralButton("确定", null).create().show();
			return;
		}
		try {
			Float.parseFloat(inpay_m);
			Integer.parseInt(count);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			new AlertDialog.Builder(InAndOutAty.this).setTitle("提示！").setMessage("数据格式填写错误，请重新输入！").setCancelable(false).setNeutralButton("确定", null).create().show();
			e.printStackTrace();
			return;
		}
		new Builder(this)
				.setTitle("提示！")
				.setMessage(
						"您确定要添加此项吗？\n"
								+ "编号ID:"
								+ kind_id
								+ "进价:"
								+ inpay_m
								+ "重量:"
								+ (Float.parseFloat(weight_m)
										* Float.parseFloat(gc_long) * Integer
											.parseInt(count))
								+ "总价:"
								+ (Float.parseFloat(inpay_m)
										* Float.parseFloat(gc_long) * Integer
											.parseInt(count)))
				.setNegativeButton("取消", null)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//添加一条进货记录，并返回添加是否成功的结果
						if (server.addInStore(id, kind_id, weight_m, gc_long,
								inpay_m, count, date)) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("id", id + "");
							map.put("kind_id", kind_id);
							map.put("inpay_m", inpay_m);
							map.put("count", count);
							map.put("weight",
									Float.parseFloat(weight_m)
											* Float.parseFloat(gc_long)
											* Integer.parseInt(count) + "");
							map.put("allpay",
									Float.parseFloat(inpay_m)
											* Float.parseFloat(gc_long)
											* Integer.parseInt(count) + "");
							inList.add(map);
							inAdapter.notifyDataSetChanged();
							in_inpay_m.setText("");
							in_count.setText("");
							Toast.makeText(InAndOutAty.this, "添加成功！",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(InAndOutAty.this, "没能添加成功！",
									Toast.LENGTH_SHORT).show();
						}

					}
				}).create().show();

	}

	/**
	 * 出货按扭事件
	 * 
	 * @param view
	 */
	public void Button_out_item(View view) {
		final long id = System.currentTimeMillis();
		final String kind_id = (String) outSpinner.getSelectedItem();
		final String weight_m = out_weight_m.getText().toString();
		final String gc_long = out_gc_long.getText().toString();
		final String outpay_m = out_outpay_m.getText().toString();
		final String count = out_count.getText().toString();
		final String date = simpleDateFormat.format(new Date());
		if (!server.isNull(kind_id, weight_m, gc_long, outpay_m, count, date)) {
			new AlertDialog.Builder(InAndOutAty.this).setTitle("提示！").setMessage("数据不能为空！").setCancelable(false).setNeutralButton("确定", null).create().show();
			return;
		}
		try {
			Float.parseFloat(outpay_m);
			Integer.parseInt(count);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			new AlertDialog.Builder(InAndOutAty.this).setTitle("提示！").setMessage("数据格式填写错误，请重新输入！").setCancelable(false).setNeutralButton("确定", null).create().show();
			e.printStackTrace();
			return;
		}

		new Builder(this)
				.setTitle("提示！")
				.setMessage(
						"您确定要添加此项吗？\n"
								+ "编号ID:"
								+ kind_id
								+ "售价:"
								+ outpay_m
								+ "重量:"
								+ (Float.parseFloat(weight_m)
										* Float.parseFloat(gc_long) * Integer
											.parseInt(count))
								+ "总价:"
								+ (Float.parseFloat(outpay_m)
										* Float.parseFloat(gc_long) * Integer
											.parseInt(count)))
				.setNegativeButton("取消", null)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						int code=server.addOutStore(id, kind_id, weight_m, gc_long,
								outpay_m, count, date);
						if (code==3) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("id", id + "");
							map.put("kind_id", kind_id);
							map.put("inpay_m", outpay_m);
							map.put("count", count);
							map.put("weight",
									Float.parseFloat(weight_m)
											* Float.parseFloat(gc_long)
											* Integer.parseInt(count) + "");
							map.put("allpay",
									Float.parseFloat(outpay_m)
											* Float.parseFloat(gc_long)
											* Integer.parseInt(count) + "");
							outList.add(map);
							outAdapter.notifyDataSetChanged();
							out_outpay_m.setText("");
							out_count.setText("");
							Toast.makeText(InAndOutAty.this, "添加成功！",
									Toast.LENGTH_SHORT).show();
						} 
						if(code==1)
						{
								Toast.makeText(InAndOutAty.this, "库存里面什么也没有！",
										Toast.LENGTH_SHORT).show();
						}
						if(code==2)
						{
								Toast.makeText(InAndOutAty.this, "出货数量不足！",
										Toast.LENGTH_SHORT).show();
						}
						if(code==4)
						{
								Toast.makeText(InAndOutAty.this, "数据表更新失败，出货失败！",
										Toast.LENGTH_SHORT).show();
						}
					}
				}).create().show();

	}

}
