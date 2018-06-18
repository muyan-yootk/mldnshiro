package cn.mldn.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GlobalAction {
	@RequestMapping("/error")
	public String error() {
		return "plugins/errors" ;
	} 
	@RequestMapping("/forward")
	public String forward() {
		return "plugins/forward" ;
	} 
	@RequestMapping("/unauthz")
	public String unauthz() {
		return "plugins/unauthz" ;
	} 
	@RequestMapping("/logoutInfo")
	public String logoutInf() {
		return "logout" ;
	} 
}
