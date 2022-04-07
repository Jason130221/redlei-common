package com.redlei.common.utils;

public class Constant {
	/** 日志ID */
	public final static String LOGGER_ID = "logId";
	/** 应用id */
	public final static String APPID = "appid";
	public final static String APPUserToken = "user_token";


	public final static String APPUserIdWithToken = "user_id_with_token";
	/** 无需token列表 */
	public final static String NOTOKENLIST = "com.onway.sms.sendSms";
	/**
	 * redis:登录tokan的键
	 */
    public  final static String PREFIX = "session:";
	/**
	 * redis:短信发送记录数的key
	 */
	public  final static String PREFIX_SMS = "sms_code";

}
