package cn.mldn.action;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mldn.util.action.AbstractAction;

@Controller
public class MemberLoginAction extends AbstractAction {

	@RequestMapping("/login_pre")
	public String loginPre(String mid, String password) {
		return super.getMessage("login.page");
	}

	@RequestMapping("/login")
	public ModelAndView login(String mid, String password) {
		ModelAndView mav = new ModelAndView(super.getMessage("login.index"));
		// 1、在Shiro里面所有的用户名和密码应该放在认证Token类之中
		AuthenticationToken token = new UsernamePasswordToken(mid, password) ;
		try { // 2、需要进行登录处理
			SecurityUtils.getSubject().login(token);
		} catch (Exception e) {
			mav.setViewName(super.getMessage("login.page")); // 登录失败跳转到错误页
			mav.addObject("msg", e.getMessage()) ; // 保存错误信息到登录页
		}
		return mav; // 跳转
	}
}
