package com.example.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.store.bean.In_store;
import com.example.store.bean.Out_store;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.server.Server;
import com.example.store.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WaterAty extends Activity implements OnClickListener{
	private Button but_water, but_store, but_home, but_seting, but_about;
	private ListView lv_water;
	private InStoreDaoInf dao;
	private Server server;
	private List<HashMap<String, String>> data;
	private ArrayList<Object> in_store_ArrayList ;
	private ArrayList<Object> out_store_ArrayList ;
	private SimpleAdapter water_Adapter;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.water);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		handler = new  Handler();
		but_water = (Button) findViewById(R.id.but_water);
		but_store = (Button) findViewById(R.id.but_store);
		but_home = (Button) findViewById(R.id.but_home);
		but_seting = (Button) findViewById(R.id.but_seting);
		but_about = (Button) findViewById(R.id.but_about);

		but_water.setOnClickListener(this);
		but_store.setOnClickListener(this);
		but_home.setOnClickListener(this);

		but_seting.setOnClickListener(this);
		but_about.setOnClickListener(this);
		
		dao = new InStoreDaoInf(this);
		server=new Server(this);
		
		
		data = new ArrayList<HashMap<String,String>>();
		lv_water = (ListView) findViewById(R.id.lv_water);
		water_Adapter = new SimpleAdapter(this, data, R.layout.item_water, new String[]{"action","kind_id","inpay_m","weight","allpay","date"},
				new int[]{R.id.action,R.id.water_kind_id,R.id.water_pay_m,R.id.water_weight,R.id.water_all_pay,R.id.water_date});
		lv_water.setAdapter(water_Adapter);
		
		
		lv_water.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
new AlertDialog.Builder(WaterAty.this).setTitle("提示！").setMessage("您确定要删除此项吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						if("进货".equals(data.get(position).get("action")))
						{
							//删除一条进货流水
							Boolean b = server.delItemFromInStore(data.get(position));
							delWaterItem(position, b);
						}else {
							//删除一条出货流水
							Boolean b = server.delItemFromOutStore(data.get(position));
							delWaterItem(position, b);
						}
					}
				}).setNegativeButton("取消", null).create().show();
				
			}
		});
		//进货流水
		in_store_water();
		
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.but_store) {
			startActivity(new Intent(WaterAty.this, StoreAty.class));
			finish();
		}
		if (v.getId() == R.id.but_home) {
			startActivity(new Intent(WaterAty.this, HomeActivity.class));
			finish();
		}
		if(v.getId()== R.id.but_seting) 
		{
			startActivity(new Intent(WaterAty.this, KindAty.class));
			finish();
		}
		if (v.getId() == R.id.but_about) {
		}
	}
	/**
	 * 进货流水按扭
	 * @param view
	 */
	public void in_water_lv(View view)
	{
		this.findViewById(R.id.but_in_water).setEnabled(false);
		this.findViewById(R.id.but_out_water).setEnabled(true);
		in_store_water();
	}
	
	/**
	 * 进货流水
	 */
	private void in_store_water() {
		in_store_ArrayList =dao.getList(In_store.class);
		data.clear();
		if(in_store_ArrayList!=null)
		{
			for (Object object : in_store_ArrayList) {
				In_store in_store = (In_store) object;
				
				System.out.println(in_store.toString());
				
				String id = in_store.getId()+"";
				String kind_id = in_store.getKind_id();
				String Inpay_m = in_store.getInpay_m()+"";
				String count = in_store.getCount()+"";
				String wight = in_store.getWight()+"";
				String allpay = in_store.getAllpay()+"";
				String date = in_store.getDate()+"";
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("action", "进货");
				map.put("id", id);
				map.put("kind_id", kind_id);
				map.put("inpay_m", Inpay_m);
				map.put("count", count);
				map.put("weight", wight);
				map.put("allpay", allpay);
				map.put("date", date);
				data.add(map);
			}
			in_store_ArrayList=null;
			
		}
		water_Adapter.notifyDataSetChanged();
		
	}
	/**
	 * 出货流水按扭
	 * @param view
	 */
	public void out_water_lv(View view)
	{
		this.findViewById(R.id.but_in_water).setEnabled(true);
		this.findViewById(R.id.but_out_water).setEnabled(false);
		out_store_water();
	}
	/**
	 * 出货流水
	 */
	private void out_store_water() {
		out_store_ArrayList = dao.getList(Out_store.class);
		data.clear();
		if(out_store_ArrayList!=null)
		{
			for (Object object : out_store_ArrayList) {
				Out_store in_store = (Out_store) object;
				String id = in_store.getId()+"";
				String kind_id = in_store.getKind_id();
				String count = in_store.getCount()+"";
				String Inpay_m = in_store.getOutpay_m()+"";
				String wight = in_store.getWight()+"";
				String allpay = in_store.getAllpay()+"";
				String date = in_store.getDate()+"";
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("action", "出货");
				map.put("id", id);
				map.put("kind_id", kind_id);
				map.put("count", count);
				map.put("inpay_m", Inpay_m);
				map.put("weight", wight);
				map.put("allpay", allpay);
				map.put("date", date);
				data.add(map);
				
			}
			out_store_ArrayList=null;
			
		}
		water_Adapter.notifyDataSetChanged();
		
		
	}
	/**
	 *判定是否删除界面列表里指定的数据 
	 * @param position 指定的数据
	 * @param b 是否删除
	 */
	public void delWaterItem(final int position, Boolean b) {
		if(b)
		{
			data.remove(position);
			water_Adapter.notifyDataSetChanged();
			Toast.makeText(WaterAty.this, "删除成功！", Toast.LENGTH_LONG).show();
		}else {
			Toast.makeText(WaterAty.this, "未能成功删除！", Toast.LENGTH_LONG).show();
		}
	}
	
}
