package cn.mldn.mldnshiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class TestShiroIni {
	private static final String USERNAME = "mldn" ;	// 用户名
	private static final String PASSWORD = "java" ;	// 密码
	@Test
	public void testAuthentication() throws Exception {	// 此时要进行认证的测试
		// 1、定义一个SecurityManager的创建工厂类（所有的信息通过ini文件读取）
		Factory<org.apache.shiro.mgt.SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:shiro.ini") ;
		// 2、有了工厂类之后就需要创建具体的SecurityManager接口对象；
		SecurityManager securityManager = securityManagerFactory.getInstance() ;
		// 3、进行系统的SecurityManager配置
		SecurityUtils.setSecurityManager(securityManager);
		// 4、通过SecurityUtils创建Subject
		Subject subject = SecurityUtils.getSubject() ;
		// 5、所有的用户登录信息都需要封装在认证Token之中；
		AuthenticationToken token = new UsernamePasswordToken(USERNAME,PASSWORD) ;
		// 6、有了认证的Token，有了Subject就可以实现认证处理
		subject.login(token);
		// 7、如果成功登录了，则一定可以获取用户名
		System.out.println(subject.getPrincipal()); // 用户名
	}
}
