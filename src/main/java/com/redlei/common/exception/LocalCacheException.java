package com.redlei.common.exception;

/**
 * 本地缓存异常
 * 
 */
public class LocalCacheException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9040812111580824579L;

	/**
	 * 构造函数，包含额外信息
	 * 
	 * @param msg 额外的信息，用于打印到日志中方便查找问题
	 */
	public LocalCacheException(String msg) {
		super(msg);
	}

	/**
	 * 构造函数，包含额外信息
	 * 
	 * @param msg   额外的信息，用于打印到日志中方便查找问题
	 * @param cause 异常
	 */
	public LocalCacheException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
