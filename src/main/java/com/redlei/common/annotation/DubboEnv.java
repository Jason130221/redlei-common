package com.redlei.common.annotation;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class DubboEnv implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7866286969275980510L;
	@Value("${dubbo.registry.address}")
	private String zookeeperUrl;// ="192.168.1.41:2181";
	@Value("${dubbo.registry.username}")
	private String userName;
	@Value("${dubbo.registry.password}")
	private String password;
	@Value("${onway.application.name}")
	private String appName = "default-application";
	private Boolean mutx;
	@Value("${dubbo.registry.protocol}")
	private DubboProtocol protocol = DubboProtocol.dubbo;

	@Value("${dubbo.application.qos-port:22223}")
	private Integer qosPort = 22222;

	@Value("${dubbo.application.qos-enable:false}")
	private Boolean qosEnable = false;

	private String localIP;

	public String getZookeeperUrl() {
		return zookeeperUrl;
	}

	public void setZookeeperUrl(String zookeeperUrl) {
		this.zookeeperUrl = zookeeperUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Boolean getMutx() {
		return mutx;
	}

	public void setMutx(Boolean mutx) {
		this.mutx = mutx;
	}

	public DubboProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(DubboProtocol protocol) {
		this.protocol = protocol;
	}

	public Integer getQosPort() {
		return qosPort;
	}

	public void setQosPort(Integer qosPort) {
		this.qosPort = qosPort;
	}

	public Boolean getQosEnable() {
		return qosEnable;
	}

	public void setQosEnable(Boolean qosEnable) {
		this.qosEnable = qosEnable;
	}

	public String getLocalIP() {
		return localIP;
	}

	public void setLocalIP(String localIP) {
		this.localIP = localIP;
	}

	public enum DubboProtocol {
		rmi, dubbo, zookeeper;

		@Override
		public String toString() {
			return name();
		}
	}
}
