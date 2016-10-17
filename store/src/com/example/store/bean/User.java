package com.example.store.bean;

public class User  extends T
{
	private String photoImageView;
	private String user_name;
	private String user_psw;
	private String gander;
	private String qq;
	private String hobbly;
	private String signature;
	public String getPhotoImageView() {
		return photoImageView;
	}
	public void setPhotoImageView(String photoImageView) {
		this.photoImageView = photoImageView;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_psw() {
		return user_psw;
	}
	public void setUser_psw(String user_psw) {
		this.user_psw = user_psw;
	}
	public String getGander() {
		return gander;
	}
	public void setGander(String gander) {
		this.gander = gander;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getHobbly() {
		return hobbly;
	}
	public void setHobbly(String hobbly) {
		this.hobbly = hobbly;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "User [photoImageView=" + photoImageView
				+ ", user_name=" + user_name + ", user_psw=" + user_psw
				+ ", gander=" + gander + ", qq=" + qq + ", hobbly=" + hobbly
				+ ", signature=" + signature + "]";
	}
	
	
	
}
