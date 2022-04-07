package com.redlei.common.response;

public enum RespCodeState {

	/**
	 * 1开始是系统错误
	 */
	API_ERROE_CODE_1001("1001", "非法应用"), API_LOGIN_ILLEGAL_AUTHOR("1002", "非法请求"),

	/**
	 * 2开始是操作成功处理
	 **/
	API_OPERATOR_SUCCESS("2000", "操作成功"), API_LOGIN_SUCCESS("2001", "登录成功"), API_REGISTER_SUCCESS("2002", "注册成功"),
	API_SENDSMS_SUCCESS("2003", "短信发送成功"), API_LOGIN_AUTHOR_SUCCESS("2100", "授权成功"),

	/**
	 * 3是操作失败处理
	 **/
	API_ERROE_CODE_3000("3000", "操作失败"),
	/**
	 * 校验签名失败
	 */
	API_CHECK_SIGN_FAIL("3001", "校验签名失败 "), API_LOGIN_FAIL("3002", "登录失败"), API_REGISTER_FAILED("3003", "注册失败"),
	API_REGISTER_USERHASEXIST("3004", "用户已存在"), API_OPERATOR_MISS_PARA("3005", "缺少必要参数"),
	/**
	 * 请求参数非法
	 */
	API_ILLEGAL_PARA("3006", "请求参数非法 "), API_SMS_ERROR("3007", "验证码错误"), API_SMS_INVALID("3008", "验证码过期"),
	API_SMS_EMPTY("3009", "验证码为空"), API_TRANSE_ERROR("3009", "获取渠道失败"), API_SENDSMS_SOFAST("3010", "短信发送过快"),

	API_SMS_SEND_FAIL("3011", "短信发送失败"),

	USER_NO_PREMISSION("3104", "请先登陆"), ADMIN_NO_PERMISSION("3105", "暂无权限"),

	ACCOUNT_LOGIN_OTHER_EQUIPMENT("3200", "账号在其他设备登陆"),

	API_REPEAT_DATA("3300", "存在相同数据"),

	THE_STORE_NAME_ALREADY__EXISTS("3301", "店铺名称已存在"),

	NOT_TO_OPEN_AN_ACCOUNT("3302", "未开户"), SUPPLY_COMMODITY_TAG("3303", "该标签下尚有商品"),
	USER_AUTH_PARAM_ERROR("3304", "认证失败，请确认参数是否正确"),

	/**
	 * 4 是授权或权限失败的处理
	 **/
	API_LOGIN_AUTHOR_INVALID("4000", "无效授权"), API_AUTHOR_INVALID("4002", "授权过期，请重新登录"),
	/**
	 * 5 原有状态
	 */
	OK("4002", "授权过期，请重新登录"), FAIL("9999", "失败"), INVALID("1111", "非法请求"), NOAUTH("2222", "无权限");

	private String statusCode;
	private String message;

	private RespCodeState(String statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public static String getResponseEnumValue(String statusCode) {
		for (RespCodeState responseEnum : RespCodeState.values()) {
			if (responseEnum.getStatusCode().equals(statusCode)) {
				return responseEnum.getMessage();
			}
		}
		return null;
	}

	public static RespCodeState getResponseEnum(String statusCode) {
		for (RespCodeState responseEnum : RespCodeState.values()) {
			if (statusCode.equals(responseEnum.getStatusCode())) {
				return responseEnum;
			}
		}
		return null;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
