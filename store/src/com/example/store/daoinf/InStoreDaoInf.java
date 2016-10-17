package com.example.store.daoinf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode.Mode;

import com.example.store.bean.GcParameter;
import com.example.store.bean.In_store;
import com.example.store.bean.Out_store;
import com.example.store.bean.Store;
import com.example.store.bean.Summary;
import com.example.store.dao.InfDao;
import com.example.store.dao_helper.DbHelper;

public class InStoreDaoInf implements InfDao {
	private Context context;
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Cursor cursor = null;

	public InStoreDaoInf(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dbHelper = new DbHelper(context);
	}

	private void close() {
		if (cursor != null)
			cursor.close();
		if (db != null)
			db.close();
		if (dbHelper != null)
			dbHelper.close();

	}

	@Override
	public boolean save(Object object) {
		// TODO Auto-generated method stub
		String sql = getSqlSave(object);
		db = dbHelper.getWritableDatabase();
		try {
			db.execSQL(sql);
			close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		return false;
	}

	/**
	 * 获取一条插入语句
	 * 
	 * @param object
	 *            要插入的对象
	 * @return 返回一条插入语句 ，失败返回NULL
	 */
	public String getSqlSave(Object object) {
		try {
			String sql = "insert into ";
			Class c = object.getClass();
			String table = c.getSimpleName();
			sql += table;
			Method[] ms = c.getDeclaredMethods();
			String keyString = "(";
			String valuesString = ")values(";
			for (Method method : ms) {
				String string = method.getName();

				if (string.startsWith("get")) {
					System.out.println(string + "~~~~~~~~~~~~~");
					try {
						String key = string.substring(3).toLowerCase();
						System.out.println("获取的" + key);
						keyString += key + ",";
						System.out.println("合并后的" + keyString);
						Object object2 = method.invoke(object);
						if (object2 instanceof String) {
							valuesString += "'" + object2 + "',";
						} else {
							valuesString += object2 + ",";
						}
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			keyString = keyString.substring(0, keyString.length() - 1);
			valuesString = valuesString.substring(0, valuesString.length() - 1);
			sql += keyString + valuesString + ")";
			System.out.println(sql);
			return sql;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean del(Object object) {
		// TODO Auto-generated method stub
		HashMap<String, String> list = getDelParameter(object);
		db = dbHelper.getWritableDatabase();
		int i = db.delete(list.get("table"),
				list.get("column").substring(3, list.get("column").length()),
				new String[] { list.get("result") });
		close();
		if (i > 0) {
			return true;
		}
		return false;

	}
	/**
	 * 获取一个根据ID删除的SQl需要的参数
	 * 
	 * @param object
	 *            要删除的对象
	 * @return 返回一个HashMap 含有mName,result,table
	 */
	private String getDelSql(Object object) {
		// TODO Auto-generated method stub
		Class c = object.getClass();
		String mName;

		String table = c.getSimpleName();
		String column = null;
		String result = null;
		Method[] ms = c.getDeclaredMethods();
		if (table.equals("In_store") || table.equals("Out_store")) {
			for (Method method : ms) {
				mName = method.getName().toLowerCase();
				if (mName.equals("getid")) {
					try {
						column  = mName.substring(3, mName.length());
						result = method.invoke(object) + "";
						break;
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		} else {
			for (Method method : ms) {
				mName = method.getName().toLowerCase();
				if (mName.equals("getkind_id")) {
					try {
						column  = mName.substring(3, mName.length());
						result = "'"+ method.invoke(object)+"'";
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

		}
		return "delete from " + table + " where " + column + "=" + result;
	}

	/**
	 * 获取一个根据ID删除的SQl需要的参数
	 * 
	 * @param object
	 *            要删除的对象
	 * @return 返回一个HashMap 含有mName,result,table
	 */
	private HashMap<String, String> getDelParameter(Object object) {
		// TODO Auto-generated method stub
		HashMap<String, String> list = new HashMap<String, String>();
		String column = null;
		String result = null;
		String mName = null;
		Class c = object.getClass();
		String table = c.getSimpleName();
		Method[] ms = c.getDeclaredMethods();
		if (table.equals("In_store") || table.equals("Out_store")) {
			for (Method method : ms) {
				mName = method.getName().toLowerCase();
				if (mName.equals("getid")) {
					try {
						column = mName;
						result = method.invoke(object) + "";
						break;
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		} else {
			for (Method method : ms) {
				mName = method.getName().toLowerCase();
				if (mName.equals("getkind_id")) {
					try {
						column = mName;
						result = (String) method.invoke(object);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

		}
		list.put("column", column += "=?");
		list.put("result", result);
		list.put("table", table);
		return list;
	}

	@Override
	public boolean upData(Object object) {
		// TODO Auto-generated method stub
		String sql = getUpDataSql(object);
		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL(sql);
			close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		return false;
	}

	/**
	 * 获取一条更新语名
	 * 
	 * @param object
	 *            更新的对象
	 * @return 返回更新语句，失败返回NULL
	 */
	public String getUpDataSql(Object object) {
		String sql = "update ";
		Class c = object.getClass();
		String table = c.getSimpleName();
		sql += table + " set ";
		String idString = null;
		Object valuesObject = null;
		Method[] methods = c.getDeclaredMethods();
		for (Method method : methods) {
			String mString = method.getName().toLowerCase();
			if (mString.startsWith("get")) {
				if (mString.endsWith("id")) {
					idString = mString.substring(3, mString.length());
					try {
						valuesObject = method.invoke(object);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					String key = mString.substring(3, mString.length());
					try {
						Object object2 = method.invoke(object);
						sql += key + "=";
						if (object2 instanceof String) {
							sql += "'" + object2 + "',";
						} else {
							sql += object2 + ",";
						}
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
		sql = sql.substring(0, sql.length() - 1);
		if (valuesObject instanceof String) {
			sql += " where " + idString + "=" + "'" + valuesObject + "'";
		} else {
			sql += " where " + idString + "=" + valuesObject;
		}

		System.out.println(sql);
		return sql;
	}

	@Override
	public ArrayList<Object> getList(Class className) {
		// TODO Auto-generated method stub
		ArrayList<Object> list = new ArrayList<Object>();
		String table = className.getSimpleName();
		String sql = "select * from " + table;
		System.out.println(sql);
		db = dbHelper.getReadableDatabase();
		cursor = db.rawQuery(sql, null);
		if (cursor.getCount() == 0) {
			close();

			return null;

		} else {
			try {
				System.out.println("此次一共获得" + cursor.getCount() + "个数据");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("GcParameter".equals(table)) {
			System.out.println("-->GcParameter<--");
			while (cursor.moveToNext()) {
				GcParameter gcParameter = new GcParameter();
				gcParameter.setKind_id(cursor.getString(cursor
						.getColumnIndex("kind_id")));
				gcParameter.setWeight_m(cursor.getFloat(cursor
						.getColumnIndex("weight_m")));
				gcParameter.setGc_long(cursor.getFloat(cursor
						.getColumnIndex("gc_long")));

				list.add(gcParameter);
				System.out
						.println("-->GcParameter<--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			}
			close();
			return list;

		} else if ("In_store".equals(table)) {

			while (cursor.moveToNext()) {
				In_store in_store = new In_store();
				in_store.setId(cursor.getLong(cursor.getColumnIndex("id")));
				in_store.setKind_id(cursor.getString(cursor
						.getColumnIndex("kind_id")));
				in_store.setWeight_m(cursor.getFloat(cursor
						.getColumnIndex("weight_m")));
				in_store.setGc_long(cursor.getFloat(cursor
						.getColumnIndex("gc_long")));
				in_store.setInpay_m(cursor.getFloat(cursor
						.getColumnIndex("inpay_m")));
				in_store.setCount(cursor.getInt(cursor.getColumnIndex("count")));
				in_store.setWight(cursor.getFloat(cursor
						.getColumnIndex("wight")));
				in_store.setAllpay(cursor.getFloat(cursor
						.getColumnIndex("allpay")));
				in_store.setDate(cursor.getString(cursor.getColumnIndex("date")));
				list.add(in_store);
			}
			close();
			return list;

		} else if ("Out_store".equals(table)) {

			while (cursor.moveToNext()) {
				Out_store out_store = new Out_store();
				out_store.setId(cursor.getLong(cursor.getColumnIndex("id")));
				out_store.setKind_id(cursor.getString(cursor
						.getColumnIndex("kind_id")));
				out_store.setWeight_m(cursor.getFloat(cursor
						.getColumnIndex("weight_m")));
				out_store.setGc_long(cursor.getFloat(cursor
						.getColumnIndex("gc_long")));
				out_store.setOutpay_m(cursor.getFloat(cursor
						.getColumnIndex("outpay_m")));
				out_store
						.setCount(cursor.getInt(cursor.getColumnIndex("count")));
				out_store.setWight(cursor.getFloat(cursor
						.getColumnIndex("wight")));
				out_store.setAllpay(cursor.getFloat(cursor
						.getColumnIndex("allpay")));
				out_store.setDate(cursor.getString(cursor
						.getColumnIndex("date")));
				list.add(out_store);
			}
			close();
			return list;
		} else if ("Store".equals(table)) {

			while (cursor.moveToNext()) {
				Store store = new Store();
				store.setKind_id(cursor.getString(cursor
						.getColumnIndex("kind_id")));
				store.setWeight_m(cursor.getFloat(cursor
						.getColumnIndex("weight_m")));
				store.setGc_long(cursor.getFloat(cursor
						.getColumnIndex("gc_long")));
				store.setCount(cursor.getInt(cursor.getColumnIndex("count")));
				store.setWight(cursor.getFloat(cursor.getColumnIndex("wight")));
				list.add(store);
			}
			close();
			return list;
		} else if ("Summary".equals(table)) {

			while (cursor.moveToNext()) {
				Summary summary = new Summary();
				summary.setWeight_all(cursor.getFloat(cursor
						.getColumnIndex("weight_all")));
				summary.setInpay_all(cursor.getFloat(cursor
						.getColumnIndex("inpay_all")));
				summary.setOutpay_all(cursor.getFloat(cursor
						.getColumnIndex("outpay_all")));
				summary.setIn_or_out_pay(cursor.getFloat(cursor
						.getColumnIndex("in_or_out_pay")));
				list.add(summary);
			}
			close();
			return list;
		}

		return null;
	}

	/**
	 * 获取新添加的记录
	 * 
	 * @return 返回一个包含新添加的记录的Json ，失败返回Null
	 */
	public JSONObject getByAddFlag() {
		JSONObject jsonObject = null;
		// List<Object> add_list = new ArrayList<Object>();
		JSONArray add_list = new JSONArray();
		JSONObject resultJsonObject = new JSONObject();
		db = dbHelper.getWritableDatabase();
		String gcsql = "select * from GcParameter where add_flag=" + 1 + "";
		String insql = "select * from In_store where add_flag=" + 1 + "";
		String outsql = "select * from Out_store where add_flag=" + 1 + "";
		String stsql = "select * from Store where add_flag=" + 1 + "";
		String susql = "select * from Summary where add_flag=" + 1 + "";
		db = dbHelper.getReadableDatabase();
		Cursor cursor = null;

		// 获取添加的种类
		try {
			cursor = db.rawQuery(gcsql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					jsonObject = new JSONObject();

					jsonObject.put("type", "gcparameter");
					jsonObject.put("kind_id",cursor.getString(cursor.getColumnIndex("kind_id")));
					jsonObject.put("weight_m",cursor.getFloat(cursor.getColumnIndex("weight_m"))+"");
					jsonObject.put("gc_long",cursor.getFloat(cursor.getColumnIndex("gc_long"))+"");

					add_list.put(jsonObject);
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			// 获取添加的入库记录
			cursor = db.rawQuery(insql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					jsonObject = new JSONObject();
					jsonObject.put("type", "in_store");
					jsonObject.put("id", cursor.getLong(cursor.getColumnIndex("id"))+"");
					jsonObject.put("kind_id",cursor.getString(cursor.getColumnIndex("kind_id")));
					jsonObject.put("weight_m",cursor.getFloat(cursor.getColumnIndex("weight_m"))+"");
					jsonObject.put("gc_long",cursor.getFloat(cursor.getColumnIndex("gc_long"))+"");
					jsonObject.put("inpay_m",cursor.getFloat(cursor.getColumnIndex("inpay_m"))+"");
					jsonObject.put("count",cursor.getInt(cursor.getColumnIndex("count"))+"");
					jsonObject.put("wight",cursor.getFloat(cursor.getColumnIndex("wight"))+"");
					jsonObject.put("allpay",cursor.getFloat(cursor.getColumnIndex("allpay"))+"");
					jsonObject.put("date",cursor.getString(cursor.getColumnIndex("date")));

					add_list.put(jsonObject);
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			// 获取添加的出货记录
			cursor = db.rawQuery(outsql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					jsonObject = new JSONObject();
					jsonObject.put("type", "out_store");
					jsonObject.put("id", cursor.getLong(cursor.getColumnIndex("id"))+"");
					jsonObject.put("kind_id",cursor.getString(cursor.getColumnIndex("kind_id")));
					jsonObject.put("weight_m",cursor.getFloat(cursor.getColumnIndex("weight_m"))+"");
					jsonObject.put("gc_long",cursor.getFloat(cursor.getColumnIndex("gc_long"))+"");
					jsonObject.put("outpay_m",cursor.getFloat(cursor.getColumnIndex("outpay_m"))+"");
					jsonObject.put("count",cursor.getInt(cursor.getColumnIndex("count"))+"");
					jsonObject.put("wight",cursor.getFloat(cursor.getColumnIndex("wight"))+"");
					jsonObject.put("allpay",cursor.getFloat(cursor.getColumnIndex("allpay"))+"");
					jsonObject.put("date",cursor.getString(cursor.getColumnIndex("date")));
					add_list.put(jsonObject);
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			// 获取添加的库库记录
			cursor = db.rawQuery(stsql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					jsonObject = new JSONObject();
					jsonObject.put("type", "store");
					jsonObject.put("kind_id",cursor.getString(cursor.getColumnIndex("kind_id")));
					jsonObject.put("weight_m",cursor.getFloat(cursor.getColumnIndex("weight_m"))+"");
					jsonObject.put("gc_long",cursor.getFloat(cursor.getColumnIndex("gc_long"))+"");
					jsonObject.put("count",cursor.getInt(cursor.getColumnIndex("count"))+"");
					jsonObject.put("wight",cursor.getFloat(cursor.getColumnIndex("wight"))+"");
					add_list.put(jsonObject);
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			cursor = db.rawQuery(susql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					jsonObject = new JSONObject();
					jsonObject.put("type", "summary");
					jsonObject.put("id",cursor.getInt(cursor.getColumnIndex("id"))+"");
					jsonObject.put("in_or_out_pay",cursor.getFloat(cursor.getColumnIndex("in_or_out_pay"))+"");
					jsonObject.put("outpay_all",cursor.getFloat(cursor.getColumnIndex("outpay_all"))+"");
					jsonObject.put("weight_all",cursor.getFloat(cursor.getColumnIndex("weight_all"))+"");
					jsonObject.put("inpay_all",cursor.getFloat(cursor.getColumnIndex("inpay_all"))+"");
					add_list.put(jsonObject);
				}
				if (cursor != null) {
					cursor.close();
				}
			}
			close();
			SharedPreferences preferences = context.getSharedPreferences(
					"user", context.MODE_APPEND);
			String string = preferences.getString("user_json", "");
			if (!"".equals(string)) {
				String flag = new JSONObject(string).getInt("id") + "";
				resultJsonObject.put("user_flag", flag);
				resultJsonObject.put("add_list", add_list);
				return resultJsonObject;
			} else {
				System.out.println("flag没有拿到！");
				return null;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
			return null;
		} finally {
			close();
		}

	}

	@Override
	public Object getById(Class className, String id) {
		// TODO Auto-generated method stub
		db = dbHelper.getWritableDatabase();
		String table = className.getSimpleName();
		String sql = "select * from " + table + " where kind_id='" + id + "'";
		System.out.println(sql);
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				if ("GcParameter".equals(table)) {
					GcParameter gcParameter = new GcParameter();
					gcParameter.setKind_id(cursor.getString(cursor
							.getColumnIndex("kind_id")));
					gcParameter.setWeight_m(cursor.getFloat(cursor
							.getColumnIndex("weight_m")));
					gcParameter.setGc_long(cursor.getFloat(cursor
							.getColumnIndex("gc_long")));
					close();
					return gcParameter;
				} else if ("In_store".equals(table)) {
					In_store in_store = new In_store();
					in_store.setKind_id(cursor.getString(cursor
							.getColumnIndex("kind_id")));
					in_store.setWeight_m(cursor.getFloat(cursor
							.getColumnIndex("weight_m")));
					in_store.setGc_long(cursor.getFloat(cursor
							.getColumnIndex("gc_long")));
					in_store.setInpay_m(cursor.getFloat(cursor
							.getColumnIndex("inpay_m")));
					in_store.setCount(cursor.getInt(cursor
							.getColumnIndex("count")));
					in_store.setWight(cursor.getFloat(cursor
							.getColumnIndex("wight")));
					in_store.setAllpay(cursor.getFloat(cursor
							.getColumnIndex("allpay")));
					in_store.setDate(cursor.getString(cursor
							.getColumnIndex("date")));
					close();
					return in_store;

				} else if ("Out_store".equals(table)) {
					Out_store out_store = new Out_store();
					out_store.setKind_id(cursor.getString(cursor
							.getColumnIndex("kind_id")));
					out_store.setWeight_m(cursor.getFloat(cursor
							.getColumnIndex("weight_m")));
					out_store.setGc_long(cursor.getFloat(cursor
							.getColumnIndex("gc_long")));
					out_store.setOutpay_m(cursor.getFloat(cursor
							.getColumnIndex("outpay_m")));
					out_store.setCount(cursor.getInt(cursor
							.getColumnIndex("count")));
					out_store.setWight(cursor.getFloat(cursor
							.getColumnIndex("wight")));
					out_store.setAllpay(cursor.getFloat(cursor
							.getColumnIndex("allpay")));
					out_store.setDate(cursor.getString(cursor
							.getColumnIndex("date")));
					close();
					return out_store;

				} else if ("Store".equals(table)) {
					Store store = new Store();
					store.setKind_id(cursor.getString(cursor
							.getColumnIndex("kind_id")));
					store.setWeight_m(cursor.getFloat(cursor
							.getColumnIndex("weight_m")));
					store.setGc_long(cursor.getFloat(cursor
							.getColumnIndex("gc_long")));
					store.setCount(cursor.getInt(cursor.getColumnIndex("count")));
					store.setWight(cursor.getFloat(cursor
							.getColumnIndex("wight")));
					close();
					return store;

				}
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		return null;
	}

	/**
	 * 启用事务更新出库表、库存表、总表
	 * 
	 * @param out_store
	 *            出库表
	 * @param store
	 *            库存表
	 * @param summary
	 *            总表
	 * @return 成功 true，失败 fale
	 */
	public boolean saveOutItemWater(Out_store out_store, Store store,
			Summary summary) {
		// TODO Auto-generated method stub
		String out_store_save_sql = getSqlSave(out_store);
		String store_sql=null;
		if(store.getCount()==0)
		{
			store_sql=getDelSql(store);
		}else {
			store_sql = getUpDataSql(store);
		}
		String summary_updata_sql = getUpDataSql(summary);
		db = dbHelper.getWritableDatabase();
		db.execSQL(out_store_save_sql);
		db.execSQL(store_sql);
		db.execSQL(summary_updata_sql);
		close();
		return true;
	}

	/**
	 * 启用事务更新进库表、库存表、总表
	 * 
	 * @param in_store
	 *            出库表
	 * @param store
	 *            库存表
	 * @param summary
	 *            总表
	 * @return 成功 true，失败 fale
	 */
	public boolean delInStoreWater(In_store in_store, Store store,
			Summary summary) {
		// TODO Auto-generated method stub
		HashMap<String, String> list = getDelParameter(in_store);
		String store_sql =null;
		if(store.getCount()==0)
		{
			store_sql=getDelSql(store);
		}else {
			store_sql=getUpDataSql(store);
		}
		String summary_updata_sql = getUpDataSql(summary);
		db = dbHelper.getWritableDatabase();
		// db.beginTransaction();
		// try {
		db.delete(list.get("table"),
				list.get("column").substring(3, list.get("column").length()),
				new String[] { list.get("result") });
		db.execSQL(store_sql);
		db.execSQL(summary_updata_sql);
		// db.setTransactionSuccessful();
		close();
		return true;
		// } catch (SQLException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// db.endTransaction();
		// close();
		// }
		// return false;
	}

	/**
	 * 清除更新记号
	 * 
	 * @return 成功true ，失败false
	 */
	public boolean cancellationUpFlag() {
		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("update GcParameter set add_flag=0,modfiy_flag=0");
			db.execSQL("update In_store set add_flag=0,modfiy_flag=0");
			db.execSQL("update Out_store set add_flag=0,modfiy_flag=0");
			db.execSQL("update Store set add_flag=0,modfiy_flag=0");
			db.execSQL("update Summary set add_flag=0,modfiy_flag=0");
			close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
			return false;
		}

	}
}
