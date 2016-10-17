package com.example.store.dao_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	public DbHelper(Context context) {
		super(context, "gangcai.db", null,3);
		// TODO Auto-generated constructor stub
		Log.i("info", "daohelp....");
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("info", "开始创表....");
		//创建钢材参数表
		db.execSQL("CREATE TABLE GcParameter ("+
					"kind_id  VARCHAR PRIMARY KEY,"+
					"weight_m DECIMAL NOT NULL,"+
					"gc_long     DECIMAL NOT NULL,"+
					"add_flag   INTEGER NOT NULL DEFAULT (0),"+
					"modfiy_flag   INTEGER NOT NULL DEFAULT (0)"+
					")");
		//创建进贷表
		db.execSQL("CREATE TABLE In_store ("+
					"id  integer PRIMARY KEY,"+
					"kind_id  VARCHAR ,"+
                     "weight_m DECIMAL NOT NULL,"+
                     "gc_long     DECIMAL NOT NULL,"+
                     "inpay_m  DECIMAL NOT NULL,"+
                     "count    INTEGER NOT NULL,"+
                     "wight    DECIMAL NOT NULL,"+
					"allpay   DECIMAL NOT NULL,"+
					"date     DATETIME NOT NULL,"+
					"add_flag   INTEGER NOT NULL DEFAULT (0),"+
					"modfiy_flag   INTEGER NOT NULL DEFAULT (0)"+
					")");
		//创建出贷表
		db.execSQL("CREATE TABLE Out_store ("+
				"id  integer PRIMARY KEY,"+
				"kind_id  VARCHAR ,"+
                 "weight_m DECIMAL NOT NULL,"+
                 "gc_long     DECIMAL NOT NULL,"+
                 "outpay_m  DECIMAL NOT NULL,"+
                 "count    INTEGER NOT NULL,"+
                 "wight    DECIMAL NOT NULL,"+
				"allpay   DECIMAL NOT NULL,"+
				"date     DATETIME NOT NULL,"+
				"add_flag   INTEGER NOT NULL DEFAULT (0),"+
				"modfiy_flag   INTEGER NOT NULL DEFAULT (0)"+
				")");
		//库存表
		db.execSQL("CREATE TABLE Store ("+
				"kind_id  VARCHAR PRIMARY KEY,"+
                 "weight_m DECIMAL NOT NULL,"+
                 "gc_long     DECIMAL NOT NULL,"+
                 "count    INTEGER NOT NULL,"+
                 "wight    DECIMAL NOT NULL,"+
                 "add_flag   INTEGER NOT NULL DEFAULT (0),"+
 				"modfiy_flag   INTEGER NOT NULL DEFAULT (0)"+
				")");
		//概要表
		db.execSQL("CREATE TABLE Summary ("+
						"id  integer PRIMARY KEY,"+
                       "weight_all DECIMAL NOT NULL,"+
                       "inpay_all  DECIMAL NOT NULL,"+
                       "outpay_all DECIMAL NOT NULL,"+
                       "in_or_out_pay  DECIMAL NOT NULL,"+
                       "add_flag   INTEGER NOT NULL DEFAULT (0),"+
       				"modfiy_flag   INTEGER NOT NULL DEFAULT (0)"+
                       ")");
		Log.i("info", "创表结束....");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i("info", "onupgrade....");
	}

}
