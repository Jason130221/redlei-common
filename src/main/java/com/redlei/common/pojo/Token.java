package com.redlei.common.pojo;

import java.io.Serializable;

public final class Token implements Serializable {

	private static final long serialVersionUID = 4689020265103494027L;
	/**
	 * 令牌ID，唯一
	 */
	private String id;

	/**
	 * 应用编号
	 */
	private String appcode;

	/**
	 * bussiness ID
	 */
	private String bid;
	/**
	 * 当前用户ID，cid
	 */
	private String userid;
	/**
	 * 当前用户名
	 */
	private String userName;
	/**
	 * 真实用户
	 */
	private String realid;
	/**
	 * 真实用户名
	 */
	private String realName;
	/**
	 * 用户账号,登录名
	 */
	private String account;
	/**
	 * 用户账号登录类型
	 */
	private String acctype;
	/**
	 * 登录部门
	 */
	private String organ;
	/**
	 * 登录部门类型
	 */
	private String organType;
	/**
	 * 登录部门名称
	 */
	private String organName;
	/**
	 * 安全码，用于验证令牌合法性
	 */
	private String secret;

	/**
	 * 返回关键要素
	 *
	 * @return
	 */
	public String getEssentialKeys() {
		return String.format("%s-%s-%s-s%", this.id, this.appcode, this.userid, this.secret);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppcode() {
		return appcode;
	}

	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealid() {
		return realid;
	}

	public void setRealid(String realid) {
		this.realid = realid;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAcctype() {
		return acctype;
	}

	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getOrganType() {
		return organType;
	}

	public void setOrganType(String organType) {
		this.organType = organType;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}