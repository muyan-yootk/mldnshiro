package cn.mldn.service;

import java.util.Map;

import cn.mldn.vo.Member;

public interface IMemberService {
	/**
	 * 根据用户编号查询出一个用户的完整信息
	 * @param mid 用户编号
	 * @return 用户对象，如果没有此用户返回为null
	 */
	public Member get(String mid) ;
	
	/**
	 * 进行登录的微服务的验证操作
	 * @param mid 用户编号
	 * @param password 登录密码
	 * @return 返回全部登录信息：
	 * 1、key = name、value = 用户真实姓名
	 */
	public Map<String,Object> login(String mid, String password) ;
}
