package cn.mldn.service;

import cn.mldn.vo.Client;

public interface IClientService {
	/**
	 * 根据clientId（client_id列）查找是否存在有客户信息
	 * @param clientId 发送给注册客户端的clientId
	 * @return 存在返回Client，否则返回null
	 */
	public Client getByClientId(String clientId) ;
}
