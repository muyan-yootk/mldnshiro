package cn.mldn.action;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/pages/admin/dept/")
public class DeptAction {
	@RequestMapping("get")
	@ResponseBody
	@RequiresAuthentication	// 必须经过认证之后才可以进行该路径的访问
	public String get() {
		return "【get】返回一个部门的详细信息。" ;
	}
	@RequestMapping("list") 
	@ResponseBody
	@RequiresAuthentication
	@RequiresRoles("dept")
	@RequiresPermissions("dept:list") 
	public String list() {
		return "【list】返回全部的部门详细信息。" ;
	}
}
