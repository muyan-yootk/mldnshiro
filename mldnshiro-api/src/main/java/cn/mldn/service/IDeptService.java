package cn.mldn.service;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;

import cn.mldn.vo.Dept;

public interface IDeptService {
	@RequiresAuthentication	// 必须经过认证之后才可以进行该路径的访问
	public Dept get(long id) ;
	@RequiresAuthentication
	@RequiresRoles("dept")
	@RequiresPermissions("dept:list") 
	public List<Dept> list() ;
}
