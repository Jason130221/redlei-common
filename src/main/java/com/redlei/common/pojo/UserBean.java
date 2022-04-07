package com.redlei.common.pojo;

import java.io.Serializable;

public final class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1258746347613611696L;
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
	 * 所属系统
	 */
	private String appcode;
	/**
	 * 当前token
	 */
	private String token;

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

	public String getAppcode() {
		return appcode;
	}

	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}