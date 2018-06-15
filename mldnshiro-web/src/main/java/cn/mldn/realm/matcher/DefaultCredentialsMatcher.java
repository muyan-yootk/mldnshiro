package cn.mldn.realm.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import cn.mldn.util.encrypt.EncryptUtil;

public class DefaultCredentialsMatcher extends SimpleCredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// 1、通过认证Token对象获取用户输入的原始密码将其转为字符串之后进行编码处理
		Object tokenPwd = EncryptUtil.encrypt(super.toString(token.getCredentials())) ;
		// 2、认证之后得到密码
		Object infoPwd = super.getCredentials(info) ; // 获取认证后的密码
		return super.equals(tokenPwd, infoPwd) ;
	}
}
 