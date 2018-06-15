package cn.mldn.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Client implements Serializable {
	private Long clid ;
	private String clientId ;
	private String clientSecret ;
	public Long getClid() {
		return clid;
	}
	public void setClid(Long clid) {
		this.clid = clid;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	@Override
	public String toString() {
		return "Client [clid=" + clid + ", clientId=" + clientId + ", clientSecret=" + clientSecret + "]";
	}
}
