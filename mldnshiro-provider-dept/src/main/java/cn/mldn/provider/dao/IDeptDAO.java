package cn.mldn.provider.dao;

import java.util.List;

import cn.mldn.vo.Dept;

public interface IDeptDAO {
	public Dept findById(Long id) ;
	public List<Dept> findAll() ;
}
