package com.example.store;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class WelcomeAty extends Activity 
{
	private SharedPreferences sp;
	private ImageView welcome_imagev;
	private ViewPager viewPager;
	private List<View> pageViewList;
	private List<View> pageTitleList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		sp = this.getSharedPreferences("user", MODE_APPEND);
		pageViewList= new ArrayList<View>();
		pageTitleList = new ArrayList<View>();
		
		//≥ı ºªØpageview pagetitle;
		pageViewList.add(getLayoutInflater().inflate(R.layout.pageview1, null));	
		pageViewList.add(getLayoutInflater().inflate(R.layout.pageview2, null));	
		pageViewList.add(getLayoutInflater().inflate(R.layout.pageview3, null));	
		pageViewList.add(getLayoutInflater().inflate(R.layout.pageview4, null));	
		pageViewList.add(getLayoutInflater().inflate(R.layout.pageview5, null));	
		pageViewList.add(getLayoutInflater().inflate(R.layout.pageview6, null));	
		pageViewList.add(getLayoutInflater().inflate(R.layout.pageview7, null));	
		pageViewList.add(getLayoutInflater().inflate(R.layout.pageview8, null));	
		
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				container.removeView(pageViewList.get(position));
				System.out.println("destroy--"+position);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				System.out.println("instantiateItem--"+position);
				container.addView(pageViewList.get(position));
				return pageViewList.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("isViewFromObject--"+pageViewList.contains(arg1));
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				System.out.println("getCount--");
				return pageViewList.size();
			}
		});
	}
	public void start_use(View view )
	{
		Editor editor =  sp.edit();
		editor.putBoolean("first", false);
		editor.commit();
		startActivity(new Intent(this, HomeTip.class));
		finish();
	}
	
	//Animation animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
}
