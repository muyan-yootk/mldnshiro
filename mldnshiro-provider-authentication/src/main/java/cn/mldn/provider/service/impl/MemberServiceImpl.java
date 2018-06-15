package cn.mldn.provider.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.mldn.provider.dao.IMemberDAO;
import cn.mldn.service.IMemberService;
import cn.mldn.vo.Member;

@Service
public class MemberServiceImpl implements IMemberService {
	@Autowired
	private IMemberDAO memberDAO ;
	
	@Override
	public Member get(String mid) {
		return this.memberDAO.findById(mid);
	}
	
	@Override
	public Map<String, Object> login(String mid, String password){
		Map<String,Object> map = new HashMap<String,Object>() ;
		Member member = this.memberDAO.findById(mid) ;
		if (member == null) {	// 用户不存在
			return map ;
		}
		map.put("name", member.getName()) ;
		return map ;
	} 

}
