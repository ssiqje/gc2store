package com.example.store.db_excle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import android.graphics.Shader.TileMode;
import android.os.Environment;

/**
 * sqlite 和excle的转换工具类
 * @author Administrator
 *
 */
public class ExcleUtil {

	/**
	 * 将数据以Excel格式保存到指定的路径下
	 * @param data  要保存的数据
	 * @param typeClass 要保存的数据类类型
	 * @param descFile  保存的路径
	 * @return 2保存成功，3保存失败
	 */
	public static int dataToExcel(ArrayList<Object> data,Class typeClass,File descFile)
	{
		if(data==null||data.isEmpty())
			{
				return 2;
			}
		try {
			Method listMethod = null;
			try {
				listMethod = typeClass.getDeclaredMethod("excelGetMessage", null);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[][] listMethodName = (String[][]) listMethod.invoke(data.get(0), null);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("sheet1");
			HSSFRow row0 = sheet.createRow(0);
			row0.createCell(0).setCellValue(listMethodName[0][0]);
			HSSFCell cell= null;
			HSSFRow row1 = sheet.createRow(1);
			for (int i = 0; i < listMethodName[1].length; i++) {
				cell = row1.createCell(i);
				cell.setCellValue(listMethodName[1][i]);
			}
			int rownum=2;
			int cellnum=0;
			for (Object object : data) {
				System.out.println("~~~~~~设置第"+rownum+"行的数据为!~~~~~~~~~");
				HSSFRow dataRow = sheet.createRow(rownum++);
				cellnum=0;
				//rowdata.delete(0, rowdata.length());
				
				for (String string : listMethodName[2]) {
					Method method = null;
					try {
						method = typeClass.getDeclaredMethod("get"+string.substring(0, 1).toUpperCase()+string.substring(1, string.length()), null);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Object  result =method.invoke(object, null);
					System.out.println("!!!!!获取到的第"+cellnum+"个单元格数据为："+result+",类类型是："+result.getClass().getSimpleName());
					if("Float".equals(result.getClass().getSimpleName())||
					   "Integer".equals(result.getClass().getSimpleName()))
					{
						dataRow.createCell(cellnum++).setCellValue(Double.parseDouble(result+""));
					}else if("Long".equals(result.getClass().getSimpleName()))
					{
						dataRow.createCell(cellnum++).setCellValue(result+"");
					}
					else {
						dataRow.createCell(cellnum++).setCellValue(result+"");
					}
					
				}
				
			}
			FileOutputStream outputStream = new FileOutputStream(descFile);
			workbook.write(outputStream);
			outputStream.close();
			return 2;
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		return 3;
		
		
//		
//		Workbook workbook = FastExcel.createWriteableWorkbook(descFile);
//		StringBuffer rowdata= new StringBuffer();
//		try {
//			workbook.open();
//			Sheet sheet = workbook.addStreamSheet("sheet1");
//			Method listMethod = null;
//			try {
//				listMethod = typeClass.getDeclaredMethod("excelGetMessage", null);
//			} catch (NoSuchMethodException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			String[][] listMethodName = (String[][]) listMethod.invoke(data.get(0), null);
//			sheet.addRow(listMethodName[0]);
//			sheet.addRow(listMethodName[1]);
//			for (Object object : data) {
//				rowdata.delete(0, rowdata.length());
//				
//				for (String string : listMethodName[2]) {
//					Method method = null;
//					try {
//						method = typeClass.getDeclaredMethod("get"+string.substring(0, 1).toUpperCase()+string.substring(1, string.length()), null);
//					} catch (NoSuchMethodException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					String  resultString =method.invoke(object, null)+"";
//					rowdata.append(resultString+",");
//					
//				}
//				String[] rowStrings=rowdata.toString().split(",");
//				for (String string : rowStrings) {
//					System.out.println(string+"~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//				}
//				sheet.addRow(rowStrings);
//			}
//			sheet.addRow(new String[]{"12","男","18","小张","1234"});
//			sheet.addRow(new String[]{"13","男","13","小张","1234"});
//			sheet.addRow(new String[]{"14","男","23","小张","1234"});
//			sheet.addRow(new String[]{"15","男","24","小张","1234"});
//			sheet.addRow(new String[]{"16","男","23","小张","1234"});
//			workbook.close();
//			return 2;
//		} catch (ExcelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 3;
	}
	
}
