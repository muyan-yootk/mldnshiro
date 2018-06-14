package cn.mldn.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
// 固定的用户名和密码设置为：“mldn/java”，如果用户名不是mldn那么就表示用户不存在，密码不是java表示密码错误
public class DefaultBasicRealm implements Realm {

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String mid = (String) token.getPrincipal() ;	// 获取用户名
		String password = new String((char[]) token.getCredentials()); // 密码
		if (!"mldn".equals(mid)) {	// 用户名不存在
			throw new UnknownAccountException("【" + mid + "】该用户名不存在，请自行注册！	");
		}
		if (!"java".equals(password)) {	// 密码错误
			throw new IncorrectCredentialsException("【" + mid + "】错误的密码无法登录！");
		}
		// 如果现在用户名和密码全部正确了，那么在此时就可以将用户输入的用户名和密码直接以认证信息的形式返回即可
		return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), this.getName());
	}
	@Override
	public String getName() {
		return "mldn-shiro-default-realm"; // 名字随便写，只是一个标记而已，没有程序的存在意义
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken ; // 指定的Token类型
	}


}
