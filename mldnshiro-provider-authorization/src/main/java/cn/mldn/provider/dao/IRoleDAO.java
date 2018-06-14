package cn.mldn.provider.dao;

import java.util.Set;

public interface IRoleDAO {
	public Set<String> findAllByMember(String mid) ;
}
