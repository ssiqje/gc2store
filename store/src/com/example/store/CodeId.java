package com.example.store;

public class CodeId {
	public static  String url="http://2806602b.nat123.net:33438/datapackage/userdata";
	
	public static String getUrl() {
		return url;
	}
	
	public static void setUrl(String url) {
		CodeId.url = url;
	}

	public static class MessageID{
		public static final int REGEDIT_USER_OK = 1;
		public static final int REGEDIT_USER_FAIL = 2;
		public static final int IS_NAME_OK = 3;
		public static final int GET_PHOTO_LIST_OK = 4;
		public static final int GET_PHOTO_ONE_OK = 5;
		public static final int LOGIN_USER_PASS=6;
		public static final int LOGIN_USER_FAIL=7;
		public  static final int UP_USER_DATA_OK = 8;
		public static final int UP_USER_DATA_FAIL = 9;
		public static final int UP_DATA_FILE_IS_ENP = 10;
		public static final int UP_DATA_FILE_IS_OK = 11;
		public static final int UP_DATA_FILE_IS_FAIL = 12;
		public static final int UN_LINE_OK=13;
		public static final int UN_LINE_FAIL=14;
		public static int UPTABLE_FAIL=15;
		public static int UPTABLE_OK=16;
		public static final int GET_ALL_DB_OK = 17;
		public static final int GET_ALL_DB_FAIL = 18;
		public static final int GET_PHOTO_LIST_FAIL =19;
		public static final int GET_PHOTO_ONE_FAIL = 20;
	} 
	public static class StartActivityID{
		public static final int USER_REGEIDT = 1;
		public static final int SET_USER_PARAMETER = 5;
		protected static final int LOGIN = 0;
	}
}
