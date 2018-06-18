package cn.mldn.realm.matcher;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;

import cn.mldn.util.cache.shiro.RedisCache;
import cn.mldn.util.encrypt.EncryptUtil;

public class DefaultCredentialsMatcher extends SimpleCredentialsMatcher {
	private String expire = "50" ; // 设置默认的失效时间
	private int maxRetryCount = 5 ; // 设置默认的尝试次数
	private RedisCache<Object, Object> retryCache;	// 设置Redis缓存类
	private RedisCache<Object, Object> autcCache;	// 设置Redis缓存类

	private void retry(String mid) {	// 配置一个用户名进行尝试次数的配置
		AtomicInteger num = (AtomicInteger) this.retryCache.get(mid) ; // 通过缓存获取当前的内容
		if (num == null) {	// 此时用户没有尝试登录过
			num = new AtomicInteger(1) ; // 第一次登录
			this.retryCache.put(mid, num) ;// 保存登录次数
			return ; // 后续不再执行
		} 
		if (num.incrementAndGet() > this.maxRetryCount) {	// 超过了次数
			this.retryCache.putEx(mid, num, this.expire) ;// 进行锁定
		} else {	// 尝试第二次
			this.retryCache.put(mid, num) ; // 保存当前的计数
		}
	}
	private void unlock(String mid) {
		this.retryCache.remove(mid) ;
	} 
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		System.out.println("token - pass = " + super.toString(token.getCredentials()));
		System.out.println("info - pass = " + super.toString(info.getCredentials()));
		// 1、通过认证Token对象获取用户输入的原始密码将其转为字符串之后进行编码处理
		Object tokenPwd = EncryptUtil.encrypt(super.toString(token.getCredentials())) ;
		String mid = token.getPrincipal().toString() ;
		this.retry(mid); ; // 进行密码尝试次数的统计
		// 2、认证之后得到密码
		Object infoPwd = super.getCredentials(info) ; // 获取认证后的密码
		boolean flag = super.equals(tokenPwd, infoPwd) ;
		if (flag) {	// 密码匹配
			this.unlock(mid); 
		} else {	// 需要考虑清除认证缓存的信息。
			this.autcCache.remove(mid) ; // 登录失败清除一下认证信息
		}
		System.out.println("********** flag = " + flag);
		return flag ;
	}
	public void setCacheManager(CacheManager cacheManager) {
		this.retryCache = (RedisCache) cacheManager.getCache("retryCache") ; 
		this.autcCache = (RedisCache) cacheManager.getCache("authenticationCache") ;
	} 
	public void setExpire(String expire) {
		this.expire = expire;
	}
	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}
} 
 