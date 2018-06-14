package cn.mldn.action;

import java.util.Map;

import javax.security.auth.login.LoginException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mldn.service.IMemberAuthorizationService;
import cn.mldn.service.IMemberService;
import cn.mldn.util.action.AbstractAction;

@Controller
public class MemberLoginAction extends AbstractAction {
	@Reference
	private IMemberService memberService;
	@Reference
	private IMemberAuthorizationService memberAorzService;

	@RequestMapping("/login_pre")
	public String loginPre(String mid, String password) {
		return super.getMessage("login.page");
	}

	@RequestMapping("/login")
	public ModelAndView login(String mid, String password) {
		ModelAndView mav = new ModelAndView(super.getMessage("login.index"));
		Map<String, Object> result = this.memberService.login(mid, password);
		Map<String, Object> role = this.memberAorzService.listByMember(mid);
		super.getSession().setAttribute("name", result.get("name"));
		super.getSession().setAttribute("mid", mid);
		super.getSession().setAttribute("allRoles", role.get("allRoles"));
		super.getSession().setAttribute("allActions", role.get("allActions"));
		mav.setViewName(super.getMessage("login.page")); // 登录失败跳转到错误页
		return mav; // 跳转
	}
}
