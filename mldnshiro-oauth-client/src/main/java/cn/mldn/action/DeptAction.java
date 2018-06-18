package cn.mldn.action;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mldn.service.IDeptService;

@Controller
@RequestMapping("/pages/admin/dept/")
public class DeptAction {
	@Reference
	private IDeptService deptSerivce ;
	@RequestMapping("get")
	@ResponseBody
	@RequiresAuthentication	// 必须经过认证之后才可以进行该路径的访问
	public Object get(long did) {
		return this.deptSerivce.get(did) ;
	}
	@RequestMapping("list") 
	@ResponseBody
	@RequiresAuthentication
	@RequiresRoles("dept")
	@RequiresPermissions("dept:list") 
	public Object list() {
		return this.deptSerivce.list() ;
 	}
}
