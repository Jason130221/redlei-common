package com.redlei.common.enums;

import com.redlei.common.utils.StringUtil;

public enum SysConfigCacheKeyEnum implements EnumBase{
	// ======流入决策=======
	/** 流入决策开关 */
	DEPOSIT_DECIDE_SWITCH("DEPOSIT_DECIDE_SWITCH", "流入决策开关"),

	/** 流入成功率最小值 */
	DEPOSIT_DECIDE_MIN_SUCC_RATE("DEPOSIT_DECIDE_MIN_SUCC_RATE", "流入成功率最小值"),

	/** 流入最大连续调用失败次数(计算成功率) */
	DEPOSIT_MAX_CONTINUE_ERROR_COUNT("DEPOSIT_MAX_CONTINUE_ERROR_COUNT", "流入最大连续调用失败次数"),

	/** 流入最近调用次数(计算成功率) */
	DEPOSIT_RECENT_CALL_COUNT("DEPOSIT_RECENT_CALL_COUNT", "流入最近调用次数"),

	/** 流入成功率低于临界值恢复的时间间隔,精确到分 */
	DEPOSIT_SUCC_RATE_RECOVER_INTERVAL("DEPOSIT_SUCC_RATE_RECOVER_INTERVAL", "流入成功率低于临界值恢复的时间间隔,精确到分"),

	// ======实名认证=======
	/** 实名认证重试次数 */
	USER_CERTIFY_MAX_RETRY_COUNT("USER_CERTIFY_MAX_RETRY_COUNT", "实名认证重试次数"),

	// ======签约验卡=======
	/** 签约验卡最大重试次数 */
	THIRD_USER_MAX_RETRY_COUNT("THIRD_USER_MAX_RETRY_COUNT", "签约验卡最大重试次数"),

	// ======流入=======
	/** 流入回查最大次数 */
	DEPOSIT_RETURN_QUERY_MAX_RETRY_COUNT("DEPOSIT_RETURN_QUERY_MAX_RETRY_COUNT", "流入回查最大次数"),

	/** 流入回查时间间隔,精确到秒 */
	DEPOSIT_RETURN_QUERY_INTERVAL("DEPOSIT_RETURN_QUERY_INTERVAL", "流入回查时间间隔,精确到秒"),

	/** 流入返回上层失败时间,精确到分钟 */
	DEPOSIT_RETURN_FAIL_INTERVAL("DEPOSIT_RETURN_FAIL_INTERVAL", "流入返回上层失败时间,精确到分钟"),

	// ======流出=======
	/** 流出回查最大次数 */
	WITHDRAW_RETURN_QUERY_MAX_RETRY_COUNT("WITHDRAW_RETURN_QUERY_MAX_RETRY_COUNT", "流出回查最大次数"),

	/** 流出回查时间间隔,精确到秒 */
	WITHDRAW_RETURN_QUERY_INTERVAL("WITHDRAW_RETURN_QUERY_INTERVAL", "流出回查时间间隔,精确到秒"),

	/** 提现定时任务推进条数 */
	WITHDRAW_TASK_PROCESS_SIZE("WITHDRAW_TASK_PROCESS_SIZE", "提现定时任务推进条数"),

	// ======渠道不可用=======
	/** 联动优势不可用对应的错误码 */
	UMPAY_FAIL_CODE("UMPAY_FAIL_CODE", "联动优势不可用对应的错误码"),

	/** 快钱(快捷)不可用对应的错误码 */
	SHORTCUT_FAIL_CODE("SHORTCUT_FAIL_CODE", "快钱(快捷)不可用对应的错误码"),

	/** 快钱不可用对应的错误码 */
	KUAIQIAN_FAIL_CODE("KUAIQIAN_FAIL_CODE", "快钱不可用对应的错误码"),

	/** 中金不可用对应的错误码 */
	CPCN_FAIL_CODE("CPCN_FAIL_CODE", "中金不可用对应的错误码 "),

	/** 银联不可用对应的错误码 */
	CHINAPAY_FAIL_CODE("CHINAPAY_FAIL_CODE", "银联不可用对应的错误码"),

	/** 翼支付不可用对应的错误码 */
	TELCOME_FAIL_CODE("TELCOME_FAIL_CODE", "翼支付不可用对应的错误码"),

	/** 捷蓝不可用对应的错误码 */
	JIELAN_FAIL_CODE("JIELAN_FAIL_CODE", "捷蓝不可用对应的错误码"),

	/** 移动不可用对应的错误码 */
	CMPAY_FAIL_CODE("CMPAY_FAIL_CODE", "移动不可用对应的错误码"),

	// -----------------------支付相关
	WE_PAY_APP_ID("WE_PAY_APP_ID", "微信公众号支付appid"),

	MENU_SERVERURL("MENU_SERVERURL", "微信菜单重定向地址"),

	WE_PAY_PARTNER_ID("WE_PAY_PARTNER_ID", "微信公众号支付Partner ID"),

	WE_PAY_APP_SECRET_ID("WE_PAY_APP_SECRET_ID", "微信公众号支付APP Secret"),

	WE_PAY_MCH_ID("WE_PAY_MCH_ID", "微信支付MCH_ID"),

	WE_APP_PAY_APP_ID("WE_APP_PAY_APP_ID", "微信APP支付appid"),

	WE_APP_PAY_PAY_MCH_ID("WE_APP_PAY_PAY_MCH_ID", "微信APP支付MCH_ID"),

	WE_APP_PAY_APP_SECRET_ID("WE_APP_PAY_APP_SECRET_ID", "微信APP支付APP Secret"),

	WE_APP_PAY_PARTNER_ID("WE_APP_PAY_PARTNER_ID", "微信APP支付Partner ID"),

	ALIPAY_PARTNER("ALIPAY_PARTNER", "支付宝支付partner"),

	ALIPAY_SELLER("ALIPAY_SELLER", "支付宝支付seller"),

	ALIPAY_PRIVATE_KEY("ALIPAY_PRIVATE_KEY", "支付宝支付privateKey"),

	WECHAT_PAY_CALL_BACK_URL("WECHAT_PAY_CALL_BACK_URL", "微信支付回调地址"),

	WECHAT_PAY_CALL_AUTH_URL("WECHAT_PAY_CALL_AUTH_URL", "微信支付授权地址"),

	APPLY_PRICE("APPLY_PRICE", "报名费用"),

	AGENT_PRICE("AGENT_PRICE", "平台对雇主发布用户每个用户收取的费用"),

	COMMISSION_RATE("COMMISSION_RATE", "佣金系统扣除率"),

	REJECT_APPLY_TEXT("REJECT_APPLY", "雇主拒绝用户推送消息"),

	ACCEPT_APPLY_TEXT("ACCEPT_APPLY_TEXT", "雇主接受用户推送消息"), ACCESS_TOKEN("ACCESS_TOKEN", "ACCESS_TOKEN标识"),

	COMMISSION_TEXT("COMMISSION_TEXT", "佣金以及报名费退还推送消息"),

	CLASSIFICATION_INDUSTRY("CLASSIFICATION_INDUSTRY", "行业分类"),

	WITHDRAW_RATE("WITHDRAW_RATE", "提现费率");

	/** 枚举码 */
	private String code;

	/** 描述信息 */
	private String message;

	private SysConfigCacheKeyEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @see com.onway.platform.common.enums.EnumBase#message()
	 */
	@Override
	public String message() {
		return message;
	}

	/**
	 * @see com.onway.platform.common.enums.EnumBase#value()
	 */
	@Override
	public Number value() {
		return null;
	}

	/**
	 * 通过枚举<code>code</code>获得枚举。
	 * 
	 * @param code 枚举编号
	 * @return
	 */
	public static SysConfigCacheKeyEnum getByCode(String code) {

		for (SysConfigCacheKeyEnum param : values()) {
			if (StringUtil.compare(param.getCode(), code)) {
				return param;
			}
		}

		return null;
	}
}
