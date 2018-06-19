package cn.mldn.provider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.mldn.provider.dao.IDeptDAO;
import cn.mldn.service.IDeptService;
import cn.mldn.vo.Dept;
@Service
public class DeptServiceImpl implements IDeptService {
	@Autowired
	private IDeptDAO deptDAO ;
	@Override
	public boolean add(Dept dept) {
		return this.deptDAO.doCreate(dept);
	}
	@Override
	public Dept get(long id) {
		return this.deptDAO.findById(id);
	}

	@Override
	public List<Dept> list() {
		return this.deptDAO.findAll();
	}

}
