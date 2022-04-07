package com.redlei.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

	/**
	 * 下载网络文件
	 * 
	 * @param basePath 存放根目录
	 * @param urlStr   文件下载地址
	 * @throws Exception
	 */
	public static Map<String, String> downLoadFile(String basePath,String date, String urlStr) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		String[] d1 = urlStr.split("[?]")[1].split("&");
		String downloadFileName = "";
		for (int i = 0; i < d1.length; i++) {
			if (d1[i].startsWith("downloadFileName")) {
				downloadFileName = d1[i].split("=")[1];
			}
		}
		URL url = new URL(urlStr);
		InputStream is = url.openStream();

		String destDirPath = basePath + "/" + date;
		resultMap.put("upPath", destDirPath);
		resultMap.put("filePath", destDirPath + "/" + downloadFileName);

		File targetFile = new File(destDirPath + "/" + downloadFileName);
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		System.out.println("--------开始下载文件--------");
		FileOutputStream out = new FileOutputStream(targetFile);
		int len;
		byte[] buf = new byte[1024];
		while ((len = is.read(buf)) != -1) {
			out.write(buf, 0, len);
			out.flush();
		}
		System.out.println("--------完成下载文件--------");
		// 关流顺序，先打开的后关闭
		is.close();
		out.close();
		return resultMap;
	}

	/**
	 * 解压文件
	 * 
	 * @param filePath    ZIP文件路径
	 * @param destDirPath 解压文件后存放地址
	 * @throws Exception
	 * @return 返回解压后的文件名
	 */
	public static List<String> unZip(String filePath, String destDirPath) throws Exception {
		List<String> fileNames = new ArrayList<String>();
		System.out.println("-------------开始解压文件----");
		File srcFile = new File(filePath);// 获取当前压缩文件
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			throw new Exception(srcFile.getPath() + "所指文件不存在");
		}
		System.out.println("-------------创建压缩文件对象---");
		ZipFile zipFile = new ZipFile(srcFile, Charset.forName("gbk"));// 创建压缩文件对象
		// 开始解压
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			System.out.println("-------------解压文件，文件名----" + entry.getName());
			if (entry.isDirectory()) {
				String dirPath = destDirPath + "/" + entry.getName();
				srcFile.mkdirs();
			} else {
				// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
				File targetFile = new File(destDirPath + "/" + entry.getName());
				// 保证这个文件的父文件夹必须要存在
				if (!targetFile.getParentFile().exists()) {
					targetFile.getParentFile().mkdirs();
				}
				targetFile.createNewFile();
				// 将压缩文件内容写入到这个文件中
				InputStream is = zipFile.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(targetFile);
				int len;
				byte[] buf = new byte[1024];
				while ((len = is.read(buf)) != -1) {
					fos.write(buf, 0, len);
				}
				// 关流顺序，先打开的后关闭
				fos.close();
				is.close();
				fileNames.add(entry.getName());
			}
			System.out.println("-------------结束解压文件，文件名----" + entry.getName());
		}
		return fileNames;
	}

//	public static void main1(String[] args) throws Exception {
//		String upPath = "D:\\tmp\\20200925";
//		String fileName = "20888119784558440156_20200925_业务明细.csv";
//		List<String> lists = readCsv(upPath + "\\" + fileName);
//		lists.remove(1);
//		List<String> resultList = new ArrayList<String>();
//		resultList.add(lists.get(0));
//		resultList.add(lists.get(1));
//		resultList.add(lists.get(2));
//		resultList.add(lists.get(3));
//		System.out.println("---------------------------");
//		String orderNos = "7997744509899046912,7997751337651339264,7997752404216709120";
//		BigDecimal tradeAmount = BigDecimal.ZERO; // 交易金额
//		BigDecimal tradeFAmount = BigDecimal.ZERO; // 退款金额
//		
//		BigDecimal reduceAmount = BigDecimal.ZERO; // 交易优惠金额
//		BigDecimal reduceFAmount = BigDecimal.ZERO; // 退款优惠金额
//		int count = 0;
//		int countF = 0;
//		System.out.println(lists.size());
//		for (int i = 4; i < lists.size()-4; i++) {
//			String[] lineText = lists.get(i).split(",");
//			if(!lineText[0].startsWith("#")) {
//				String orderNo = lineText[1].trim();
//				BigDecimal amount = new BigDecimal(lineText[11]);
//				BigDecimal amountSS = new BigDecimal(lineText[12]);
//				if(orderNos.contains(orderNo)) {
//					String yw = lineText[2].trim();
//					if("交易".equals(yw)) {
//						count++;
//						tradeAmount = tradeAmount.add(amount);
//						reduceAmount =reduceAmount.add(amount).subtract(amountSS);
//					}else if("退款".equals(yw)) {
//						countF++;
//						tradeFAmount = tradeFAmount.add(amount);
//						reduceFAmount =reduceFAmount.add(amount).subtract(amountSS);
//					}
//					resultList.add(lists.get(i));
//				}
//			}
//		}
//		String tradeText = "#交易合计："+count+"笔，交易金额共"+tradeAmount+"元，优惠金额共"+reduceAmount+"元";
//		String reduceText = "#退款合计："+countF+"笔，交易退款金额共"+tradeFAmount+"元，优惠退款金额共"+reduceFAmount+"元";
//		resultList.add(tradeText);
//		resultList.add(reduceText);
//		resultList.forEach(s->System.out.println(s));
//		// 创建csv文件
//		createCSVFile(resultList, "D:\\tmp\\20200925_copy", "20888119784558440156_20200925_业务明细");
//	}
	
	public static List<String> readCsv(String filePath) throws IOException {
		System.out.println("读取csv文件------------->开始");
		List<String> lines = new ArrayList();
		
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				isr = new InputStreamReader(new FileInputStream(file), "GBK");
				br = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
  					lines.add(lineTxt);
				}
			} else {
			}
		} catch (Exception e) {
		} finally {
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
		}
		System.out.println("读取csv文件------------->结束");
		return lines;
	}

	/**
	 * 生成为CVS文件
	 *
	 * @param exportData 源数据List
	 * @param outPutPath 输出文件路径
	 * @param fileName   输出文件名称
	 * @return
	 */
	public static File createCSVFile(List<String> exportData, String outPutPath, String fileName) {
		System.out.println("创建csv文件");
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		try {
			File file = new File(outPutPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 定义文件名格式并创建
			csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
			csvFileOutputStream = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(csvFile), Charset.forName("UTF-8")), 1024);
			for (String d:exportData) {
				writeRow(d.split(","), csvFileOutputStream);
				csvFileOutputStream.newLine();
			}
			System.out.println("创建csv文件结束");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (csvFileOutputStream != null) {
					csvFileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return csvFile;
	}

	/**
	 * 写一行数据
	 *
	 * @param row       数据列表
	 * @param csvWriter
	 * @throws IOException
	 */
	private static void writeRow(String[] row, BufferedWriter csvWriter) throws IOException {
		int i = 0;
		for (String data : row) {
			csvWriter.write(data);
			if (i != row.length - 1) {
				csvWriter.write(",");
			}
			i++;
		}
	}
}
