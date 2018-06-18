package cn.mldn.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.cache.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mldn.util.cache.shiro.manager.RedisCacheManager;

@ContextConfiguration(locations = { "classpath:spring-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)

public class TestRedis {
	@Autowired
	private JedisConnectionFactory factory;

	@Test
	public void testConn() {
		// System.out.println(this.factory);
		RedisCacheManager rcm = new RedisCacheManager();
		Map<String,JedisConnectionFactory> map = new HashMap<>() ;
		map.put("activeSessionCache", this.factory) ;
		rcm.setConnectionFactoryMap(map);
		Cache<Object, Object> cache = rcm.getCache("activeSessionCache");
		cache.put("session-id", "Hello") ;
	}
}
