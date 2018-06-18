package cn.mldn.filter.token;

import org.apache.shiro.authc.UsernamePasswordToken;

@SuppressWarnings("serial")
public class OAuthToken extends UsernamePasswordToken { 
	private String principal ;
	private String authcode ;
	public OAuthToken(String authcode) {
		this.authcode = authcode ;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	} 
	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}
	@Override
	public Object getCredentials() {
		return this.authcode;
	}
}
