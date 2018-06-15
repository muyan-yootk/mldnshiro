package cn.mldn.service;

import java.util.List;

import cn.mldn.vo.Dept;

public interface IDeptService {
	public Dept get(long id) ;
	public List<Dept> list() ;
}
