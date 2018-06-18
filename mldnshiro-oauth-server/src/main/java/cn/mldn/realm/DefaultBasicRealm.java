package cn.mldn.realm;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mldn.service.IMemberAuthorizationService;
import cn.mldn.service.IMemberService;
import cn.mldn.util.encrypt.EncryptUtil;
import cn.mldn.vo.Member;
// 固定的用户名和密码设置为：“mldn/java”，如果用户名不是mldn那么就表示用户不存在，密码不是java表示密码错误
public class DefaultBasicRealm extends AuthorizingRealm {
	@Reference
	private IMemberService authcService ;
	private Cache<Object,Object> cache = null ;
	private int maxRetryCount = 5 ; // 设置默认的尝试次数
	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	} 
	private boolean checkRetry(String mid) {	// 检测用户是否可以登录
		AtomicInteger num = (AtomicInteger) this.cache.get(mid) ; // 通过缓存获取当前的内容
		if (num == null) {	// 此时用户没有尝试登录过
			return true ; // 可以进行数据库登录认证
		}
		if (num.intValue() > this.maxRetryCount) {	// 已经达到了最大的检测次数
			return false ; // 不允许进行数据库认证
		} 
		return true ;
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		this.cache = super.getCacheManager().getCache("retryCache") ; // 获取一个指定的Cache对象
		String mid = (String) token.getPrincipal() ;	// 获取用户名
		if (this.checkRetry(mid)) {	// 检测是否可以登录
			System.out.println("***************** 【1、DefaultBasicRealm - 认证检测】doGetAuthenticationInfo");
			// 用户认证信息处理（如果要与RPC或者业务层相连接就在此处实现调用）
			// 调用业务层中的方法进行认证检测，如果成功则返回name的数据
			Member member = this.authcService.get(mid) ;	// 获取一个用户信息
			if (member == null) {	// 用户名不存在
				throw new UnknownAccountException("【" + mid + "】该用户名不存在，请自行注册！	");
			}
			if (member.getLocked().equals(1)) {	// 账户被锁定
				throw new LockedAccountException("【" + mid + "】该账户已经被管理员锁定，无法登录！") ;
			}
			// 对输入的密码进行加密处理操作。
			SecurityUtils.getSubject().getSession().setAttribute("name", member.getName());
			// 如果现在用户名和密码全部正确了，那么在此时就可以将用户输入的用户名和密码直接以认证信息的形式返回即可
			return new SimpleAuthenticationInfo(token.getPrincipal(), member.getPassword(), this.getName());
		} else {
			throw new LockedAccountException("【" + mid + "】该账户已经被管理员锁定，无法登录！") ;
		}
	}
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 进行授权的统一管理（如果要与RPC或者业务层相连接就在此处实现调用）
		// 用户登录认证的时候需要用户名和密码，但是用户授权信息获取的时候只需要用户名即可
		String mid = (String) principals.getPrimaryPrincipal() ; // 获取用户名
		System.out.println("***************** 【2、DefaultBasicRealm - 授权检测】doGetAuthorizationInfo，用户名：" + mid);
		SimpleAuthorizationInfo authz = new SimpleAuthorizationInfo() ;
		return authz ; 
	} 

}
