package cn.mldn.provider.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.mldn.provider.dao.IActionDAO;
import cn.mldn.provider.dao.IRoleDAO;
import cn.mldn.service.IMemberAuthorizationService;
@Service
public class MemberAuthorizationServiceImpl implements IMemberAuthorizationService {
	@Autowired
	private IRoleDAO roleDAO ;
	@Autowired
	private IActionDAO actionDAO ;
	@Override
	public Map<String, Object> listByMember(String mid) {
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("allRoles", this.roleDAO.findAllByMember(mid)) ;
		map.put("allActions", this.actionDAO.findAllByMember(mid)) ;
		return map ;
	}

} 
