package cn.mldn.action;

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
	public Object get(long did) {
		return this.deptSerivce.get(did) ;
	}
	@RequestMapping("list") 
	@ResponseBody
	public Object list() {
		return this.deptSerivce.list() ;
 	}
}
