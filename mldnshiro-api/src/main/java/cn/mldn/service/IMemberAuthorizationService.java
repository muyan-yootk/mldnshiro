package cn.mldn.service;

import java.util.Map;

public interface IMemberAuthorizationService {
	/**
	 * 实现一个用户授权信息的获取，可以抓取角色与权限信息
	 * @param mid 登录后的mid
	 * @return 包含有如下内容：
	 * 1、key = allRoles、value = 全部的角色信息；
	 * 2、key = allActions、value = 全部的权限信息。
	 */
	public Map<String,Object> listByMember(String mid) ;
}
