package com.example.store.bean;

import java.util.ArrayList;

public class Out_store extends T
{
	private long id;
	private String kind_id;
	private float weight_m;
	private float gc_long;
	private float outpay_m;
	private int count;
	private float wight;
	private float allpay;
	private String date;
	private int add_flag;
	private int modfiy_flag;
	
	public Out_store(){}
	
	public Out_store(long id, String kind_id, float weight_m, float gc_long,
			float outpay_m, int count, float wight, float allpay, String date,
			int add_flag, int modfiy_flag) {
		super();
		this.id = id;
		this.kind_id = kind_id;
		this.weight_m = weight_m;
		this.gc_long = gc_long;
		this.outpay_m = outpay_m;
		this.count = count;
		this.wight = wight;
		this.allpay = allpay;
		this.date = date;
		this.add_flag = add_flag;
		this.modfiy_flag = modfiy_flag;
	}



	@Override
	public String toString() {
		return "Out_store [id=" + id + ", kind_id=" + kind_id + ", weight_m="
				+ weight_m + ", gc_long=" + gc_long + ", outpay_m=" + outpay_m
				+ ", count=" + count + ", wight=" + wight + ", allpay="
				+ allpay + ", date=" + date + ", add_flag=" + add_flag
				+ ", modfiy_flag=" + modfiy_flag + "]";
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getKind_id() {
		return kind_id;
	}



	public void setKind_id(String kind_id) {
		this.kind_id = kind_id;
	}



	public float getWeight_m() {
		return weight_m;
	}



	public void setWeight_m(float weight_m) {
		this.weight_m = weight_m;
	}



	public float getGc_long() {
		return gc_long;
	}



	public void setGc_long(float gc_long) {
		this.gc_long = gc_long;
	}



	public float getOutpay_m() {
		return outpay_m;
	}



	public void setOutpay_m(float outpay_m) {
		this.outpay_m = outpay_m;
	}



	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}



	public float getWight() {
		return wight;
	}



	public void setWight(float wight) {
		this.wight = wight;
	}



	public float getAllpay() {
		return allpay;
	}



	public void setAllpay(float allpay) {
		this.allpay = allpay;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public int getAdd_flag() {
		return add_flag;
	}



	public void setAdd_flag(int add_flag) {
		this.add_flag = add_flag;
	}



	public int getModfiy_flag() {
		return modfiy_flag;
	}



	public void setModfiy_flag(int modfiy_flag) {
		this.modfiy_flag = modfiy_flag;
	}



	public String[][] excelGetMessage()
	{
		return new String[][]{
								{"出库表"},
								{"流水编号","种类编号","米/吨","根/M","售价","根数","总重(吨)","总付款","日期"},
								{"id","kind_id","weight_m","gc_long","outpay_m","count","wight","allpay","date"}
								
							  };
		
	}
	
}
