package com.redlei.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.redlei.common.annotation.Constants;

public class HttpRequest {
	private static final String SERVLET_POST = "POST";
	private static final String SERVLET_GET = "GET";
	private static final String REQUEST_SEC = "MD5";

	/**
	 * 
	 * 
	 */
	public static String doPost(String urlStr, Map<String, Object> paramMap, String charSet, int sendType,
			Boolean needPrepare) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_POST);
		setUrlconnection(conn, charSet, sendType);
		String paramStr = preparePostParam(paramMap, sendType, needPrepare);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		DataOutputStream os = new DataOutputStream(conn.getOutputStream());
		os.write(paramStr.toString().getBytes(charSet));
		os.flush();
		os.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		br.close();
		return result;
	}

	

	public static String doOriginPost(String urlStr, String body, int sendType) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_POST);
		setUrlconnection(conn, "UTF-8", sendType);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		DataOutputStream os = new DataOutputStream(conn.getOutputStream());
		os.write(body.getBytes("UTF-8"));
		os.flush();
		os.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		br.close();
		return result;
	}

	/**
	 * get请求
	 * 
	 * @param urlStr
	 * @param paramMap
	 * @param charSet
	 * @param sendType    1发送text,2发送xml,3发送json
	 * @param needPrepare 是否需要自身系统参数拼装
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String urlStr, Map<String, Object> paramMap, String charSet, int sendType,
			Boolean needPrepare) throws Exception {

		if (needPrepare) {
			String paramStr = urlStr;
			prepareParam(paramMap);
			if (paramStr == null || paramStr.trim().length() < 1) {

			} else {
				urlStr += "?" + paramStr;
			}
		}

		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(SERVLET_GET);
		setUrlconnection(conn, charSet, sendType);
		conn.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		String result = "";
		while ((line = br.readLine()) != null) {
			result += line;
		}
		br.close();
		return result;
	}

	/**
	 * GET参数预处理
	 * 
	 * 
	 */
	private static String prepareParam(Map<String, Object> param) {
		// 加密参数
		Map<String, Object> paramMap = RSAParams(param);
		// 拼装sign
		CheckSign(paramMap);
		String result = "";
		StringBuffer sb = new StringBuffer();
		if (paramMap.isEmpty()) {
			return "";
		} else {
			for (String key : paramMap.keySet()) {
				String value = (String) paramMap.get(key);
				if (value == null || value.isEmpty()) {
					continue;
				}
				try {
					value = URLEncoder.encode(value, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (sb.length() < 1) {
					sb.append(key).append("=").append(value);
				} else {
					sb.append("&").append(key).append("=").append(value);
				}
			}
			result = sb.toString();
		}
		// result+="7YxEXknepy22F7sVazSHuMbsuTzKcwTe";
		System.err.println(result);
		return result;
	}

	private static String preparePostParam(Map<String, Object> param, int sendType, Boolean needPrepare) {
		if (needPrepare) {
			// 加密参数
			Map<String, Object> paramMap = RSAParams(param);
			// 拼装sign
			CheckSign(paramMap);
			return JSONObject.toJSONString(paramMap);
		}

		return JSONObject.toJSONString(param);
	}

	private static void setUrlconnection(HttpURLConnection connection, String charSet, int sendType) {
		// 设置通用的请求属性
		if (charSet == null || charSet.isEmpty()) {
			charSet = "UTF-8";
		}
		connection.setRequestProperty("Charsert", charSet);
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("appid", Constants.LEMUJI_APPID);
		switch (sendType) {
		case 1:// Text
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + UUID.randomUUID().toString());
			break;
		case 2:// XML
			connection.setRequestProperty("Content-Type", "text/xml;");
			break;
		case 3:// Json
			connection.setRequestProperty("Content-Type", "application/json;");
			break;
		default:
			break;
		}
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

	}

	/**
	 * RSA参数封装
	 * 业务参数都封装在params内，最终请求参数只有method,signature(可不传，默认md5),appid,params,sign(在后面加入)
	 * 
	 * @param param
	 * @return
	 **/
	private static Map<String, Object> RSAParams(Map<String, Object> param) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		if (!param.containsKey("method")) {
//			return paramsMap;
//		}

		String result = SignatureUtil.getSignString(param);
//		result = result.replace('"', '\'');
		try {
			String paramString = RSAUtil.sign(result, "utf-8", Constants.LEMUJI_PUBLICE_KEY, true, true);
			paramsMap.put("params", paramString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		paramsMap.put("method", param.get("method"));
		if (param.containsKey("signature")) {
			paramsMap.put("signature", param.get("signature"));
		}
		paramsMap.put("issecurity", "1");
		paramsMap.put("appid", param.get("appid"));
		return paramsMap;
	}

	/**
	 * 计算sign
	 * 
	 * @param params
	 */
	private static void CheckSign(Map<String, Object> params) {
		String signString = SignatureUtil.getSignString(params);
		String checkSign = null;
		if (StringUtil.isEmpty(signString)) {
			return;
		}
		switch (REQUEST_SEC) {
		case "MD5":
			checkSign = MD5Util.getMd5(signString);
			break;
		default:
			checkSign = MD5Util.getMd5(signString);
			break;
		}

		params.put("sign", checkSign);
	}

}
