package cn.mldn.realm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
// 固定的用户名和密码设置为：“mldn/java”，如果用户名不是mldn那么就表示用户不存在，密码不是java表示密码错误
public class DefaultBasicRealm extends AuthorizingRealm {
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("***************** 【1、DefaultBasicRealm - 认证检测】doGetAuthenticationInfo"); 
		// 用户认证信息处理（如果要与RPC或者业务层相连接就在此处实现调用）
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
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 进行授权的统一管理（如果要与RPC或者业务层相连接就在此处实现调用）
		// 用户登录认证的时候需要用户名和密码，但是用户授权信息获取的时候只需要用户名即可
		String mid = (String) principals.getPrimaryPrincipal() ; // 获取用户名
		System.out.println("***************** 【2、DefaultBasicRealm - 授权检测】doGetAuthorizationInfo，用户名：" + mid);
		SimpleAuthorizationInfo authz = new SimpleAuthorizationInfo() ;
		// 实际的开发应该通过业务层获取所有的角色信息
		Set<String> allRoles = new HashSet<String>() ;
		allRoles.addAll(Arrays.asList("dept","emp","member")) ; // 添加角色信息
		Set<String> allActions = new HashSet<String>() ;
		allActions.addAll(Arrays.asList("dept:add","dept:list","emp:edit","member:admin")) ;
		authz.setRoles(allRoles); // 保存角色，保存到Shiro自己的管理机制里
		authz.setStringPermissions(allActions); // 保存权限信息
		return authz ;
	}

}
