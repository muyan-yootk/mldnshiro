package cn.mldn.service;

import java.util.Map;

public interface IMemberService {
	/**
	 * 进行登录的微服务的验证操作
	 * @param mid 用户编号
	 * @param password 登录密码
	 * @return 返回全部登录信息：
	 * 1、key = name、value = 用户真实姓名
	 */
	public Map<String,Object> login(String mid, String password) ;
}
