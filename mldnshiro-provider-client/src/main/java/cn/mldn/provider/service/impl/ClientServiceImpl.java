package cn.mldn.provider.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.mldn.provider.dao.IClientDAO;
import cn.mldn.service.IClientService;
import cn.mldn.vo.Client;

@Service
public class ClientServiceImpl implements IClientService {
	@Resource
	private IClientDAO clientDAO;

	@Override
	public Client getByClientId(String clientId) {
		return this.clientDAO.findByClientId(clientId);
	}  

}
