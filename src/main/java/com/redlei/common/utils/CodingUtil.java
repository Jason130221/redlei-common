package com.redlei.common.utils;

import java.io.UnsupportedEncodingException;

public class CodingUtil {
	public static byte[] base64Decode(String str) {
		return Base64.decode(str);
	}

	public static String base64Encode(byte[] data) {
		return Base64.encode(data);
	}

	private static final int BIT_SIZE = 0x10;
	private static final int BIZ_ZERO = 0X00;

	private static char[][] charArrays = new char[256][];

	static {
		int v;
		char[] ds;
		String temp;
		for (int i = 0; i < charArrays.length; i++) {
			ds = new char[2];
			v = i & 0xFF;
			temp = Integer.toHexString(v);
			if (v < BIT_SIZE) {
				ds[0] = '0';
				ds[1] = temp.charAt(0);
			} else {
				ds[0] = temp.charAt(0);
				ds[1] = temp.charAt(1);
			}
			charArrays[i] = ds;
		}
	}

	public static String bytesToHexString(byte[] src) {
		HexAppender helper = new HexAppender(src.length * 2);
		if (src == null || src.length <= BIZ_ZERO) {
			return null;
		}
		int v;
		char[] temp;
		for (int i = 0; i < src.length; i++) {
			v = src[i] & 0xFF;
			temp = charArrays[v];
			helper.append(temp[0], temp[1]);
		}
		return helper.toString();
	}

	public static String bytesToHexStringSub(byte[] src, int length) {
		HexAppender helper = new HexAppender(src.length * 2);
		if (src == null || src.length <= BIZ_ZERO) {
			return null;
		}
		int v;
		char[] temp;
		for (int i = 0; i < length; i++) {
			v = src[i] & 0xFF;
			temp = charArrays[v];
			helper.append(temp[0], temp[1]);
		}
		return helper.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (StringUtil.isEmpty(hexString)) {
			return null;
		}
		int length = hexString.length() / 2;
		byte[] d = new byte[length];
		int pos;
		for (int i = 0; i < length; i++) {
			pos = i * 2;
			d[i] = (byte) (charToByte(hexString.charAt(pos)) << 4 | charToByte(hexString.charAt(pos + 1)));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) (c < 58 ? c - 48 : c < 71 ? c - 55 : c - 87);
	}

	private static class HexAppender {

		private int offerSet = 0;
		private char[] charData;

		public HexAppender(int size) {
			charData = new char[size];
		}

		public void append(char a, char b) {
			charData[offerSet++] = a;
			charData[offerSet++] = b;
		}

		@Override
		public String toString() {
			return new String(charData, 0, offerSet);
		}
	}

	public static String bytesToHexString(byte[] src, int startWith) {
		HexAppender helper = new HexAppender((src.length - startWith) * 2);
		if (src == null || src.length <= BIZ_ZERO) {
			return null;
		}
		int v;
		char[] temp;
		for (int i = startWith; i < src.length; i++) {
			v = src[i] & 0xFF;
			temp = charArrays[v];
			helper.append(temp[0], temp[1]);
		}
		return helper.toString();
	}

	public static byte[] hex2byte(byte[] b) {
		if (b.length % 2 != 0)
			throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[(n / 2)] = ((byte) Integer.parseInt(item, 16));
		}
		return b2;
	}

	public static String Utf8ToGbk(String str) {
		try {
			String encoding = getEncoding(str);
			System.out.println(encoding);
			String utf8 = new String(str.getBytes("UTF-8"));
			System.out.println(utf8);
			String unicode = new String(utf8.getBytes(), "UTF-8");
			System.out.println(unicode);
			String gbk = new String(unicode.getBytes("GB2312"));
			System.out.println(unicode);
			return gbk;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}

	public static String GbkToGbk(String str) {
		try {
			String encoding = getEncoding(str);
			String utf8 = new String(str.getBytes(encoding));
			String unicode = new String(utf8.getBytes(), "GBK");
			return unicode;
		} catch (Exception e) {
		}
		return str;
	}

	public static String getGB2312ToGbk(String gbStr) {
		String str;
		try {
			str = new String(gbStr.getBytes("gb2312"), "gbk");
			return str;
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}

	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}

	public static String getBASE64(String s) {
		if (s == null)
			return null;
		try {
			return new String(java.util.Base64.getEncoder().encode(s.getBytes()), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block

		}
		return "";
	}

	public static String getFromBASE64(String s) {
		if (s == null)
			return null;

		try {
			byte[] b = java.util.Base64.getDecoder().decode(s.getBytes("utf-8"));
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
}
