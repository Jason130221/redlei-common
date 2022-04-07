package com.redlei.common.utils;

import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONObject;

public class SignatureUtil {
	public static String getSignatureStr(Map<String, String> respMap) {
		return getSignatureStr(respMap, ":", "|", 1);
	}

	public static String getRSASignatureStr(Map<String, String> respMap) {
		return getSignatureStr(respMap, "=", "&", 0);
	}

	public static String getSignString(Map<String, Object> respMap) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.putAll(respMap);
		StringBuffer signature = new StringBuffer();
		for (String key : map.keySet()) {
			Object v = map.get(key);
			if (v != null && StringUtil.notEmpty(v + "")) {
				String value = JSONObject.toJSONString(map.get(key));
				if (value.startsWith("\"")) {
					value = v.toString();
				}
				signature.append(key + "=" + value + "&");
			}
		}
		return signature.toString().substring(0, signature.length() - 1);
	}

	/**
	 * 检验参数拼装
	 * 
	 * @param respMap   参数
	 * @param valueChar key value连接符
	 * @param splitChar 组参数拼接符
	 * @param lowerCase 是否需要转成大小写 0:不需要，1小写，2大写
	 * @return
	 */
	public static String getSignatureStr(Map<String, String> respMap, String valueChar, String splitChar,
			int lowerCase) {
		Map<String, String> map = new TreeMap<String, String>();
		map.putAll(respMap);
		StringBuffer signature = new StringBuffer();
		for (String key : map.keySet()) {
			if (!StringUtil.isEmpty(map.get(key)) && !"sign".equals(key) && !"issecurity".equals(key)) {
				signature.append(key + valueChar + map.get(key) + splitChar);
			}
		}
		if (StringUtil.isEmpty(signature.toString())) {
			return null;
		}
		String result = signature.toString().substring(0, signature.length() - splitChar.length());
		switch (lowerCase) {
		case 0:

			break;
		case 1:
			result = result.toLowerCase();
			break;
		case 2:
			result = result.toUpperCase();
			break;
		default:
			break;
		}
		return result;
	}

	public static String getTokenSianatureStr(Map<String, String> respMap) {
		Map<String, String> map = new TreeMap<String, String>();
		map.putAll(respMap);
		StringBuffer signature = new StringBuffer();
		for (String key : map.keySet()) {
			if (!StringUtil.isEmpty(map.get(key)) && !"token".equals(key)) {
				signature.append(key + ":" + map.get(key) + "|");
			}
		}
		return signature.toString().substring(0, signature.length() - 1).toLowerCase();
	}
}
