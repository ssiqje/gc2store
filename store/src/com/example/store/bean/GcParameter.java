package com.example.store.bean;

public class GcParameter extends T
{
	private String kind_id;
	private float weight_m;
	private float gc_long;
	private int add_flag;
	private int modfiy_flag;
	
	public GcParameter(){}
	
	public GcParameter(String kind_id, float weight_m, float gc_long,
			int add_flag, int modfiy_flag) {
		super();
		this.kind_id = kind_id;
		this.weight_m = weight_m;
		this.gc_long = gc_long;
		this.add_flag = add_flag;
		this.modfiy_flag = modfiy_flag;
	}

	@Override
	public String toString() {
		return "GcParameter [kind_id=" + kind_id + ", weight_m=" + weight_m
				+ ", gc_long=" + gc_long + ", add_flag=" + add_flag
				+ ", modfiy_flag=" + modfiy_flag + "]";
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

	/**
	 * 获取中文表名，种文字段名，英文字段名
	 * @return 返回中文表名，种文字段名，英文字段名
	 */
	public String[][] excelGetMessage()
	{
		return new String[][]{
								{"种类表"},
								{"种类编号","米/吨","根/M"},
								{"kind_id","weight_m","gc_long"}
								
							  };
		
	}
	
}
