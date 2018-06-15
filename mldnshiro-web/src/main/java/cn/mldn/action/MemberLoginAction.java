package cn.mldn.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mldn.util.action.AbstractAction;

@Controller
public class MemberLoginAction extends AbstractAction {

	@RequestMapping("/loginShiro")
	public String loginPre(String mid, String password) {
		System.out.println("*******************************");
		return super.getMessage("login.page");
	}

//	@RequestMapping("/login")
//	public ModelAndView login(String mid, String password) {
//		ModelAndView mav = new ModelAndView(super.getMessage("login.index"));
//		// 1、在Shiro里面所有的用户名和密码应该放在认证Token类之中
//		AuthenticationToken token = new UsernamePasswordToken(mid, password) ;
//		try { // 2、需要进行登录处理
//			SecurityUtils.getSubject().login(token);
//		} catch (Exception e) {
//			mav.setViewName(super.getMessage("login.page")); // 登录失败跳转到错误页
//			mav.addObject("msg", e.getMessage()) ; // 保存错误信息到登录页
//		}
//		return mav; // 跳转
//	}
}
