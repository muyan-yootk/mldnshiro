package cn.mldn.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mldn.util.action.AbstractAction;

@Controller
@RequestMapping("/pages/")
public class IndexAction extends AbstractAction {

	@RequestMapping("index")
	public String index() {
		return super.getMessage("login.index");
	}

}
