package com.example.store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.example.store.bean.GcParameter;
import com.example.store.bean.In_store;
import com.example.store.bean.Out_store;
import com.example.store.bean.Store;
import com.example.store.bean.Summary;
import com.example.store.daoinf.InStoreDaoInf;
import com.example.store.db_excle.ExcleUtil;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class testAty extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
	}
    public void save(View v)
    {
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    	//File file = new  File("/mnt/sdcard/爱你.txt");
    	File kindFile = new File(Environment.getExternalStorageDirectory(),"种类表.xls");
    	File storeFile = new File(Environment.getExternalStorageDirectory(),"库储表.xls");
    	File inStoreFile = new File(Environment.getExternalStorageDirectory(),"进货表.xls");
    	File outStoreFile = new File(Environment.getExternalStorageDirectory(),"出货表.xls");
    	File summaryFile = new File(Environment.getExternalStorageDirectory(),"总表.xls");
//    	if(!file.exists())
//    	{
//    		try {
//				if(file.createNewFile())
//				{Toast.makeText(this, "文件创建成功！", Toast.LENGTH_LONG).show();}else {
//					Toast.makeText(this, "文件创建失败！", Toast.LENGTH_LONG).show();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}else {
//    		Toast.makeText(this, "文件已存在！", Toast.LENGTH_LONG).show();
//		}
    	InStoreDaoInf daoInf = new InStoreDaoInf(this);
    	Toast.makeText(this, ExcleUtil.dataToExcel(daoInf.getList(GcParameter.class),GcParameter.class,kindFile), Toast.LENGTH_LONG).show();
    	Toast.makeText(this, ExcleUtil.dataToExcel(daoInf.getList(Store.class),Store.class,storeFile), Toast.LENGTH_LONG).show();
    	Toast.makeText(this, ExcleUtil.dataToExcel(daoInf.getList(In_store.class),In_store.class,inStoreFile), Toast.LENGTH_LONG).show();
    	Toast.makeText(this, ExcleUtil.dataToExcel(daoInf.getList(Out_store.class),Out_store.class,outStoreFile), Toast.LENGTH_LONG).show();
    	Toast.makeText(this, ExcleUtil.dataToExcel(daoInf.getList(Summary.class),Summary.class,summaryFile), Toast.LENGTH_LONG).show();
    	
    		
    }
}
