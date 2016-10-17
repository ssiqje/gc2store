package com.example.store.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.store.CodeId;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

public class Network_util {
	

	/**
	 * 注册一个用户
	 * @param url 请求地址
	 * @param object 注册对象
	 * @param handler 
	 * @return 返回HANDLER MSG,成功返回 WHAT=REGEDIT_USER_OK  失败返回 WHAT=REGEDIT_USER_FAIL
	 */
	public static void regeditUser(final String url, final JSONObject object,final Handler handler) 
	{
		new Thread(new Runnable() 
		{

			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				try {
					
						try {
							HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
							
							connection.setReadTimeout(1000 * 10);
							connection.setRequestMethod("POST");
							OutputStream outputStream = connection.getOutputStream();
							outputStream.write(("action=user_regedit&userJson=" + object.toString()).getBytes());
								BufferedReader brReader = new BufferedReader(
										new InputStreamReader(connection
												.getInputStream()));
								StringBuffer sbBuffer = new StringBuffer();
								String lineString = null;
								while ((lineString = brReader.readLine()) != null) {
									sbBuffer.append(lineString);
								}
								String resultString = sbBuffer.toString();
								if (!"".equals(resultString)) {
									JSONObject resulJsonObject = new JSONObject(
											resultString);
									System.out.println("接收到注册成功的JSON:"
											+ resulJsonObject.toString());
									String result = resulJsonObject.getString("result");
									if("pass".equals(result))
									{
										handler.sendMessage(handler.obtainMessage(
												CodeId.MessageID.REGEDIT_USER_OK, resulJsonObject));
									}else {
										handler.sendMessage(handler.obtainMessage(
												CodeId.MessageID.REGEDIT_USER_FAIL, "注册失败！"));
									}
								}


						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							handler.sendMessage(handler.obtainMessage(
									CodeId.MessageID.REGEDIT_USER_FAIL, "注册失败！"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							handler.sendMessage(handler.obtainMessage(
									CodeId.MessageID.REGEDIT_USER_FAIL, "注册失败！"));
						}
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}).start();
	}
	/**
	 * 获取服务器上的照片列表
	 * @param handler
	 * @return 返回HANDLER MSG,成功返回 WHAT=GET_PHOTO_LIST_OK
	 */
	public static void getPhotoList(final Handler handler) {
		// TODO Auto-generated method stub
		final ArrayList<HashMap<String, Object>> array_list_photo = new ArrayList<HashMap<String, Object>>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(
							"http://192.168.254.101/datapackage/userdata?action=get_photo_add_list")
							.openConnection();
					connection.setReadTimeout(1000 * 5);
					connection.setConnectTimeout(1000 * 5);
					connection.setRequestMethod("GET");
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject photo_infJsonObject = new JSONObject(
								sbBuffer.toString());
						String action = photo_infJsonObject.getString("action");
						if ("get_photo_add_list".equals(action)) {
							if ("ok".equals(photo_infJsonObject
									.getString("result"))) {
								JSONArray jsonArray = photo_infJsonObject
										.getJSONArray("json_message");
								for (int i = 0; i < jsonArray.length(); i++) {
									Object object = jsonArray.get(i);
									System.out.println("netwoke:" + object);
									String urlString = "http://192.168.254.101/"
											+ object;
									System.out.println("url:" + urlString);
									connection = (HttpURLConnection) new URL(
											urlString).openConnection();
									System.out.println();
									connection.setReadTimeout(1000 * 5);
									connection.setRequestMethod("GET");
									Bitmap bitmap = BitmapFactory
											.decodeStream(connection
													.getInputStream());
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("bitmap", bitmap);
									map.put("url", urlString);
									map.put("title", urlString.subSequence(
											urlString.lastIndexOf("/"),
											urlString.length()));
									array_list_photo.add(map);
								}
								handler.sendMessage(handler.obtainMessage(
										CodeId.MessageID.GET_PHOTO_LIST_OK, array_list_photo));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * 从服务器上获取指定的照片
	 * @param url 照片地址
	 * @return 返回HANDLER MSG,成功返回 WHAT=GET_PHOTO_ONE_OK
	 */
	public static void getPhotoBuyOne(final Handler handler, final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpURLConnection connection = (HttpURLConnection) new URL(
							url).openConnection();
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					connection.setRequestMethod("GET");
					Bitmap bitmap = BitmapFactory.decodeStream(connection
							.getInputStream());
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("url", url);
					map.put("bitmap", bitmap);
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.GET_PHOTO_ONE_OK,
							map));

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}).start();
	}

	/**
	 * 检验用户是否可以登入
	 * @param handler
	 * @param user_json 待检验的用户
	 * @return 返回HANDLER MSG,成功返回 WHAT=LOGIN_USER_PASS  失败返回 WHAT=LOGIN_USER_FAIL
	 */
	public static void login_user(final Handler handler, final String user_json) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(
							"http://192.168.254.101/datapackage/userdata")
							.openConnection();
					connection.setReadTimeout(1000 * 10);
					connection.setConnectTimeout(1000 * 10);
					connection.setRequestMethod("POST");
					OutputStream outputStream = connection.getOutputStream();
					outputStream
							.write(("action=login_user&user_json=" + user_json)
									.getBytes());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),"utf-8"));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject login_resultJsonObject = new JSONObject(
								sbBuffer.toString());
						System.out.println("登入用户资料查找结果："+login_resultJsonObject.toString());
						String action = login_resultJsonObject
								.getString("action");
						if ("login_user".equals(action)) {
							if ("pass".equals(login_resultJsonObject
									.getString("result"))) {
								

								handler.sendMessage(handler.obtainMessage(
										CodeId.MessageID.LOGIN_USER_PASS, login_resultJsonObject.getString("json_message")));
							}else {
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, login_resultJsonObject.getString("json_message")));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, "json:网络异常，请稍候在试！"));
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, "MalformedURL:网络异常，请稍候在试！"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.LOGIN_USER_FAIL, "IO:网络异常，请稍候在试！"));
				}

			}
		}).start();
	}

	/**
	 * 更新用户资料
	 * @param handler
	 * @param userJsonObject
	 */
	public static void upUser(final Handler handler, final JSONObject userJsonObject) {
		// TODO Auto-generated method stub
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(
							"http://192.168.254.101/datapackage/userdata")
							.openConnection();
					connection.setReadTimeout(1000 * 5);
					connection.setConnectTimeout(1000 * 5);
					connection.setRequestMethod("POST");
					OutputStream outputStream = connection.getOutputStream();
					outputStream
							.write(("action=up_user_data&user_json=" + userJsonObject.toString())
									.getBytes());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),"utf-8"));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject login_resultJsonObject = new JSONObject(
								sbBuffer.toString());
						System.out.println("用户资料更新结果："+login_resultJsonObject.toString());
						String action = login_resultJsonObject
								.getString("action");
						if ("up_user_data".equals(action)) {
							if ("pass".equals(login_resultJsonObject
									.getString("result"))) {
								

								handler.sendMessage(handler.obtainMessage(
										CodeId.MessageID.UP_USER_DATA_OK, "数据上传成功！"));
							}else {
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_USER_DATA_FAIL, "数据上传失败！"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_USER_DATA_FAIL, "json:网络异常，请稍候在试！"));
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_USER_DATA_FAIL, "MalformedURL:网络异常，请稍候在试！"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_USER_DATA_FAIL, "IO:网络异常，请稍候在试！"));
				}

			}
		}).start();
		
		
		
	}

//	/**
//	 * 上传数据到服务器
//	 * @param path 上传的文件路径
//	 * @return 1,文件为空，2成功，3失败
//	 */
//	public static  void  dataToServer(File path,Handler handler) {
//		// TODO Auto-generated method stub
//		if(path==null)
//		{
//			handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_DATA_FILE_IS_ENP, "文件为空！"));
//		}else {
//			int count=1;
//			if(path.isDirectory())
//			{
//				File [] filelist = path.listFiles();
//				for (; count <= filelist.length; count++) {
//					dataToServerGo(filelist[count-1],handler);
//				}
//			}else {
//				dataToServerGo(path,handler);
//			}
//		}
//		
//	}
//
//	public static void dataToServerGo(final File file,final Handler handler) {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				HttpURLConnection connection = null;
//					    String end = "/r/n";
//					    String Hyphens = "--";
//					    String boundary =UUID.randomUUID().toString();
//					    try
//					    {
//					      URL url = new URL("http://192.168.254.101/datapackage/userdata");
//					      HttpURLConnection con = (HttpURLConnection) url.openConnection();
//					      /* 允许Input、Output，不使用Cache */
//					      con.setDoInput(true);
//					      con.setDoOutput(true);
//					      con.setUseCaches(false);
//					      /* 设定传送的method=POST */
//					      con.setRequestMethod("POST");
//					      /* setRequestProperty */
//					      con.setRequestProperty("Connection", "Keep-Alive");
//					      con.setRequestProperty("Charset", "UTF-8");
//					      con.setRequestProperty("Content-Type",
//					          "multipart/form-data;boundary=" + boundary);
//					      /* 设定DataOutputStream */
//					      DataOutputStream ds = new DataOutputStream(con.getOutputStream());
//					      ds.writeBytes("action=updata");
//					      ds.writeBytes(Hyphens + boundary + end);
//					      ds.writeBytes("Content-Disposition: form-data; "
//					          + "name=\"file1\";filename=\"" + "sdfe" + "\"" + end);
//					      ds.writeBytes(end);
//					      /* 取得文件的FileInputStream */
//					      FileInputStream fStream = new FileInputStream(file);
//					      /* 设定每次写入1024bytes */
//					      int bufferSize = 1024;
//					      byte[] buffer = new byte[bufferSize];
//					      int length = -1;
//					      /* 从文件读取数据到缓冲区 */
//					      while ((length = fStream.read(buffer)) != -1)
//					      {
//					        /* 将数据写入DataOutputStream中 */
//					        ds.write(buffer, 0, length);
//					      }
//					      ds.writeBytes(end);
//					      ds.writeBytes(Hyphens + boundary + Hyphens + end);
//					      fStream.close();
//					      ds.flush();
//					      /* 取得Response内容 */
////					      InputStream is = con.getInputStream();
////					      int ch;
////					      StringBuffer b = new StringBuffer();
////					      while ((ch = is.read()) != -1)
////					      {
////					        b.append((char) ch);
////					      }
////					      is.close();
////					      ds.close();
////					      if(b.toString().equals("ok"))
////					      {
//					    	  //handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_DATA_FILE_IS_OK, "上传成功！"));
//					      System.out.println("上传成功");
////					      }
////					      handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UP_DATA_FILE_IS_FAIL, "上传失败！"));
//					    } catch (Exception e)
//					    {
//					    	e.printStackTrace();
//					    	System.out.println("上传失败");
//					    }
//			}
//
//		}).start();
//	}


	/**
	 * 用户下线更新
	 * @param user_json 下线的用户JSON
	 * @param handler 
	 */
	public static void unLine(final String user_json, final Handler handler) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(
							"http://192.168.254.101/datapackage/userdata")
							.openConnection();
					connection.setReadTimeout(1000 * 5);
					connection.setConnectTimeout(1000 * 5);
					connection.setRequestMethod("POST");
					OutputStream outputStream = connection.getOutputStream();
					outputStream
							.write(("action=un_line&user_json=" + user_json)
									.getBytes());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),"utf-8"));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject login_resultJsonObject = new JSONObject(
								sbBuffer.toString());
						System.out.println("用户资料更新结果："+login_resultJsonObject.toString());
						String action = login_resultJsonObject
								.getString("action");
						if ("un_line".equals(action)) {
							if ("pass".equals(login_resultJsonObject
									.getString("result"))) {
								
								System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~"+handler);
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_OK, "您已下线！"));
							}else {
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_FAIL, "服务器舍不得你下，那你在玩会吧。"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_FAIL, "json:网络异常，请稍候在试！"));
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_FAIL, "MalformedURL:网络异常，请稍候在试！"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UN_LINE_FAIL, "IO:网络异常，请稍候在试！"));
				}

			}
		}).start();
	}
	public static void upDataToServer(final JSONObject jsonObject, final Handler handler) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(
							"http://192.168.254.101/datapackage/userdata")
							.openConnection();
					connection.setReadTimeout(1000 * 5);
					connection.setConnectTimeout(1000 * 5);
					connection.setRequestMethod("POST");
					OutputStream outputStream = connection.getOutputStream();
					outputStream
							.write(("action=uptable&up_data_json_string=" + jsonObject.toString())
									.getBytes());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),"utf-8"));
					StringBuffer sbBuffer = new StringBuffer();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						sbBuffer.append(lineString);
					}
					try {
						JSONObject updata_resultJsonObject = new JSONObject(
								sbBuffer.toString());
						System.out.println("数据上传结果："+updata_resultJsonObject.toString());
						String action = updata_resultJsonObject
								.getString("action");
						if ("uptable".equals(action)) {
							if ("pass".equals(updata_resultJsonObject
									.getString("result"))) {
								
								System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~"+handler);
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_OK, "数据上传成功！"));
							}else {
								handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_FAIL, "数据 上传失败了，请稍后重试。"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_FAIL, "json:网络异常，请稍候在试！"));
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_FAIL, "MalformedURL:网络异常，请稍候在试！"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					handler.sendMessage(handler.obtainMessage(CodeId.MessageID.UPTABLE_FAIL, "IO:网络异常，请稍候在试！"));
				}

			}
		}).start();
	}
	
	
}
