package cn.mldn.filter.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class DefaultFormAuthenticationFilter extends FormAuthenticationFilter {
	private String randName = "rand" ;	// Session中生成的验证码属性名称
	private String codeParam = "code" ; // 表单参数名称
	private String randMessageKeyAttribute = "error" ;
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 配置整体的拒绝策略，如果验证码出现了不匹配的情况，那么后续的验证不应该再继续执行
		// 1、验证码的核心是基于Session操作的数据存储，所以应该首先获得session的输入内容；
		HttpSession session = ((HttpServletRequest) request).getSession();
		// 2、通过session获取输入的验证码内容
		String rand = (String) session.getAttribute(this.randName) ;
		// 3、获得用户输入的code内容
		String code = request.getParameter(this.codeParam) ; 
		if (rand == null || "".equals(rand) || code == null || "".equals(code)) {
			request.setAttribute(this.randMessageKeyAttribute, "验证码不允许为空！");
			return true ; // 返回true表示拒绝
		} else {	// 进行验证码的匹配处理
			if (!rand.equalsIgnoreCase(code)) {	// 验证码不匹配
				request.setAttribute(this.randMessageKeyAttribute, "验证码输入错误，无法登录！");
				return true ; 
			}
		}
		return super.onAccessDenied(request, response);
	}
	public void setRandMessageKeyAttribute(String randMessageKeyAttribute) {
		this.randMessageKeyAttribute = randMessageKeyAttribute;
	}
	public void setRandName(String randName) {
		this.randName = randName;
	}
	public void setCodeParam(String codeParam) {
		this.codeParam = codeParam;
	}
}
