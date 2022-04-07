package com.redlei.common.enums;

public enum BaseResultCodeEnum implements EnumBase {
	SUCCESS("SUCCESS", "操作成功"), SYSTEM_ERROR("SYSTEM_ERROR", "系统异常，请联系管理员！"),
	INTERFACE_SYSTEM_ERROR("INTERFACE_SYSTEM_ERROR", "外部接口调用异常，请联系管理员！"),
	CONNECT_TIME_OUT("CONNECT_TIME_OUT", "系统超时，请稍后再试!"), SYSTEM_FAILURE("SYSTEM_FAILURE", "系统错误"),
	NULL_ARGUMENT("NULL_ARGUMENT", "参数为空"), ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "参数不正确"),
	LOGIC_ERROR("LOGIC_ERROR", "逻辑错误"), DATA_ERROR("DATA_ERROR", "数据异常"),
	TRADE_MONEY_ERROR("TRADE_MONEY_ERROR", "交易金额不正确");

	private String code;
	private String message;

	private BaseResultCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static BaseResultCodeEnum getResultCodeEnumByCode(String code) {
		BaseResultCodeEnum[] arrayOfBaseResultCodeEnum;
		int j = (arrayOfBaseResultCodeEnum = values()).length;
		for (int i = 0; i < j; i++) {
			BaseResultCodeEnum param = arrayOfBaseResultCodeEnum[i];
			if (param.getCode().equals(code)) {
				return param;
			}
		}
		return null;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String message() {
		return this.message;
	}

	public Number value() {
		return null;
	}
}
