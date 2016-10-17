package com.example.store.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import android.os.Handler;

public class UploadThread extends Thread {
		private Handler handler;
		private File file;
		private String fileName;
		private String url;
		private String boundary="055a9eed-ca27-45bd-922e-9f5928767438";
		private String prefix="--";
		String end="\r\n";
		public UploadThread(File file,String url,Handler handler)
		{
			this.handler=handler;
			this.file=file;
			fileName=file.getName();
			System.out.println("获取到的文件名称："+fileName);
			this.url=url;
		}
		@Override
		public void run() {
		// TODO Auto-generated method stub
			try {
				URL httpUrl = new URL(url);
				HttpURLConnection connection  = (HttpURLConnection) httpUrl.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection .setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
				DataOutputStream outputStream= new DataOutputStream(connection.getOutputStream());
				outputStream.writeBytes(prefix+boundary+end);
				outputStream.writeBytes("Content-Disposition:form-data;name=\"file\";filename=\""+new String(fileName.getBytes(),"ISO-8859-1")+"\""+end+end);
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] b = new byte[1024*1];
				int lenght=-1;
				while((lenght=fileInputStream.read(b, 0,b.length))!=-1)
				{
					outputStream.write(b, 0, lenght);
				}
//				lenght=fileInputStream.read(b, 0,b.length);
//				outputStream.write(b, 0, lenght);
				
				
				outputStream.writeBytes(end+prefix+boundary+prefix+end);
				outputStream.flush();
				fileInputStream.close();
				outputStream.close();
				BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//				String result=null;
//				StringBuffer sb = new StringBuffer();
//				while((result=bReader.ready())!=null)
//				{
//					sb.append(result);//				}
				
						
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
}
