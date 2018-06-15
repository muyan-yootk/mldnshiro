package cn.mldn.provider.dao;

import cn.mldn.vo.Client;

public interface IClientDAO { 
	public Client findByClientId(String clientId) ; 
}
