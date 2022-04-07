package com.redlei.common.response;

import java.io.Serializable;

public class Result implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3689031409108092697L;

	/**
	 * 是否成功
	 */

	private boolean isSuccess;

	/**
	 * 错误代码
	 */
	private String statusCode;
	/**
	 * 错误信息
	 */
	private String message;

	/**
	 * 数据集
	 */
	private Object data = "";
	/**
	 * token
	 */
	private String token;

	/**
	 * 返回格式
	 */
	private RespType respType = RespType.JSON;

	public <T> Result dtoResult(T t) {
		this.setData(t);
		this.setSuccess(true);
		return this;
	}

	public String getToken() {
		return token;
	}

	public Result setToken(String token) {
		this.token = token;
		return this;
	}

	public Result() {
		this.isSuccess = false;
		this.statusCode = RespCodeState.API_ERROE_CODE_3000.getStatusCode();
		this.message = RespCodeState.API_ERROE_CODE_3000.getMessage();
	}

	public Result(boolean isSuccess) {
		this.isSuccess = isSuccess;
		if (isSuccess) {
			this.statusCode = RespCodeState.API_OPERATOR_SUCCESS.getStatusCode();
			this.message = RespCodeState.API_OPERATOR_SUCCESS.getMessage();
		} else {
			this.statusCode = RespCodeState.API_ERROE_CODE_3000.getStatusCode();
			this.message = RespCodeState.API_ERROE_CODE_3000.getMessage();
		}

	}

	public Result(boolean isSuccess, String statusCode, String message) {
		this.isSuccess = isSuccess;
		this.statusCode = statusCode;
		this.message = message;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public Result setSuccess(boolean isSuccess) {
		if (statusCode == RespCodeState.API_ERROE_CODE_3000.getStatusCode() || statusCode == null) {
			if (isSuccess) {
				this.statusCode = RespCodeState.API_OPERATOR_SUCCESS.getStatusCode();
				this.message = RespCodeState.API_OPERATOR_SUCCESS.getMessage();
			} else {
				this.statusCode = RespCodeState.API_ERROE_CODE_3000.getStatusCode();
				this.message = RespCodeState.API_ERROE_CODE_3000.getMessage();
			}
		}

		this.isSuccess = isSuccess;
		return this;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public Result setStatusCode(String statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Result setMessage(String message) {
		this.message = message;
		return this;
	}

	public Object getData() {
		return data;
	}

	public Result setData(Object data) {
		this.data = data;
		return this;
	}

	public RespType getRespType() {
		return respType;
	}

	public Result setRespType(RespType respType) {
		this.respType = respType;
		return this;
	}

	public enum RespType {
		NORMAL, JSON, XML, SECURITY
	}
}
