package com.example.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.store.bean.Store;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class StoreAty extends Activity implements OnClickListener
{
	
	private InStoreDaoInf dao;
	private ListView lv_store ;
	private ArrayList<Object> list;
	private List<HashMap<String, String>> data;
	private Button but_water, but_store, but_home, but_seting, but_about;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.store);
		//Ê¼Êµ»¯
		init();
		
	}
	private void init() {
		dao=new InStoreDaoInf(this);
		data = new ArrayList<HashMap<String,String>>();
		
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
		
		ArrayList<Object> list = dao.getList(Store.class);
		if(list!=null)
		{
			for (Object object : list) {
				Store store = (Store) object;
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("kind_id", store.getKind_id());
				map.put("count", store.getCount()+"");
				map.put("weight", store.getWight()+"");
				data.add(map);
			}
		}
		lv_store = (ListView) findViewById(R.id.lv_store);
		lv_store.setAdapter(new SimpleAdapter(this, data, R.layout.item_store, new String[]{"kind_id","count","weight"}, new int[]{R.id.store_kind_id,R.id.store_count,R.id.store_weight}));
	}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if (v.getId() == R.id.but_water) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			startActivity(new Intent(StoreAty.this, WaterAty.class));
			finish();
		}
		if (v.getId() == R.id.but_home) {
			startActivity(new Intent(StoreAty.this, HomeActivity.class));
			finish();
		}
		if(v.getId()== R.id.but_seting) 
		{
			startActivity(new Intent(StoreAty.this, KindAty.class));
			finish();
		}
		if (v.getId() == R.id.but_about) {
		}
	}
	
}
