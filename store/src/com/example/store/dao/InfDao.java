package com.example.store.dao;

import java.util.List;

public interface InfDao 
{
	/**
	 * 增加一条记录
	 * @param object 要添加的对象
	 * @return 成功 true，失败 false
	 */
	public boolean save(Object object);
	/**
	 * 删除一个对象
	 * @param object 要删除的对象
	 * @return 成功 true，失败 false
	 */
	public boolean del(Object object);
	/**
	 * 更新一个对象
	 * @param object 要更新的对象
	 * @return 成功 true，失败 false
	 */
		public boolean upData(Object object);
	/**
	 * 获取列表
	 * @return 返回列表
	 */
	public List<Object> getList(Class className);
	/**
	 * 获取指定ID项
	 * @param className 类型
	 * @param id ID
	 * @return  返回指定对象
	 */
	public Object	getById(Class className,String id);
	
	
	
	

}
