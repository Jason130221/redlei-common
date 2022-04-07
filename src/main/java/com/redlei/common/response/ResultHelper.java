package com.redlei.common.response;

import com.redlei.common.utils.StringUtil;

/**
 * @remark 返回实体
 */
public final class ResultHelper {

	/**
	 * 请求成功
	 *
	 * @return Reply
	 */
	public static Result success() {
		Result reply = new Result();
		reply.setSuccess(true);
		reply.setMessage("请求成功");

		return reply;
	}

	/**
	 * 请求成功
	 *
	 * @param obj 数据
	 * @return Reply
	 */
	public static Result success(Object obj) {
		Result reply = new Result();
		reply.setSuccess(true);
		reply.setData(obj);
		reply.setMessage("请求成功");

		return reply;
	}

	public static <T> SingleResult<T> successWithT(T obj) {
		SingleResult<T> result = new SingleResult<>();
		result.setSuccess(true);
		result.setResult(obj);
		return result;
	}

	public static <T> SingleResult<T> errorWithT() {
		SingleResult<T> result = new SingleResult<>();
		result.setSuccess(false);
		return result;
	}

	public static <T> SingleResult<T> errorWithT(T obj) {
		SingleResult<T> result = new SingleResult<>();
		result.setSuccess(false);
		result.setResult(obj);
		return result;
	}

	/**
	 * 请求成功
	 *
	 * @param obj 数据
	 * @return Reply
	 */
	public static Result success(Object obj, String msg) {
		Result reply = new Result();
		reply.setSuccess(true);
		reply.setData(obj);
		reply.setMessage(msg);

		return reply;
	}

	/**
	 * 
	 * @date 2019/11/21
	 * @remark 根据状态
	 **/
	public static Result success(Boolean result) {
		Result reply = new Result();
		reply.setSuccess(result);

		return reply;
	}

	/**
	 * 错误信息
	 *
	 * @param msg
	 * @return
	 */
	public static Result error(String msg, String code) {
		Result reply = new Result();
		reply.setStatusCode(code);
		reply.setMessage(msg);
		reply.setSuccess(false);
		return reply;
	}

	public static Result error(String msg) {
		Result reply = new Result();
		reply.setStatusCode(RespCodeState.FAIL.getStatusCode());
		reply.setMessage(msg);
		reply.setSuccess(false);
		return reply;
	}

	public static Result setReturn(boolean flag) {
		return setReturn(flag, null, null);
	}

	public static Result setReturn(boolean flag, String msg, Object obj) {
		Result reply = new Result();
		if (flag) {
			if (StringUtil.notEmpty(msg))
				reply.setMessage(msg);
			reply.setSuccess(true);
			if (obj != null) {
				reply.setData(obj);
			}
			return reply;
		} else {
			if (StringUtil.notEmpty(msg))
				reply.setMessage(msg);
			return reply;
		}
	}

	/**
	 * 错误
	 *
	 * @return Reply
	 */
	public static Result error() {
		Result reply = new Result();
		reply.setStatusCode(RespCodeState.FAIL.getStatusCode());
		reply.setSuccess(false);

		return reply;
	}

	/**
	 * 错误信息
	 *
	 * @param msg
	 * @return
	 */
	public static String errorString(String msg) {
		return String.format("{\n" + "    \"success\": false,\n" + "    \"code\": \"9999\",\n"
				+ "    \"message\": \"%s\",\n" + "    \"data\": null,\n" + "    \"option\": null\n" + "}", msg);
	}

	/**
	 * 错误信息
	 *
	 * @param code
	 * @param msg
	 * @return
	 */
	public static String errorString(String code, String msg) {
		return String.format("{\n" + "    \"success\": false,\n" + "    \"code\": \"%s\",\n"
				+ "    \"message\": \"%s\",\n" + "    \"data\": null,\n" + "    \"option\": null\n" + "}", code, msg);
	}
}
