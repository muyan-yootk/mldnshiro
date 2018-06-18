package cn.mldn.util.cache.shiro.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import cn.mldn.util.cache.shiro.RedisCache;
import cn.mldn.util.cache.shiro.abs.AbstractRedisCache;
/**
 * 根据配置的JedsConnection连接对象，进行缓存数据的保存，由于整个项目里面会存在有若干Jedis连接池，那么所有的连接通过
 * Map集合进行保存，在进行缓存配置的时候通过Map集合配置的连接名字来获取相应Redis连接对象
 * @author mldn
 *
 */
public class RedisCacheManager implements CacheManager {
	// 建立一个负责管理所有缓存处理类的集合操作，要求保证线程安全
	private final ConcurrentMap<String, Cache<Object,Object>> CACHES = new ConcurrentHashMap<>() ;
	private Map<String,JedisConnectionFactory> connectionFactoryMap ;
	@Override
	public Cache<Object, Object> getCache(String name) throws CacheException {
		Cache<Object,Object> cache = CACHES.get(name) ; 
		if (cache == null) {	// 还没有创建该缓存管理的对象就需要进行对象的创建处理
			AbstractRedisCache<Object, Object> abstractCache = null ;
			if ("authenticationCache".equals(name)) {	// 要获取的是认证缓存
				abstractCache = new RedisCache<Object,Object>() ;
				abstractCache.setConnectionFactory(this.connectionFactoryMap.get("authenticationCache"));
			} else if ("authorizationCache".equals(name)) { // 获得授权缓存
				abstractCache = new RedisCache<Object,Object>() ;
				abstractCache.setConnectionFactory(this.connectionFactoryMap.get("authorizationCache"));
			} else if ("activeSessionCache".equals(name)) { 	// 获得session缓存activeSessionCache
				abstractCache = new RedisCache<Object,Object>() ;
				abstractCache.setConnectionFactory(this.connectionFactoryMap.get("activeSessionCache"));
			} else if ("retryCache".equals(name)) { 	// 获得session缓存activeSessionCache
				abstractCache = new RedisCache<Object,Object>() ;
				abstractCache.setConnectionFactory(this.connectionFactoryMap.get("retryCache"));
			} else if ("kickoutCache".equals(name)) { 	// 获得session缓存activeSessionCache
				abstractCache = new RedisCache<Object,Object>() ;
				abstractCache.setConnectionFactory(this.connectionFactoryMap.get("kickoutCache"));
			} else if ("authcodeCache".equals(name)) { 	// 获得session缓存authcodeCache
				abstractCache = new RedisCache<Object,Object>() ;
				abstractCache.setConnectionFactory(this.connectionFactoryMap.get("authcodeCache"));
			} else if ("tokenCache".equals(name)) { // 获得session缓存authcodeCache
				abstractCache = new RedisCache<Object, Object>();
				abstractCache.setConnectionFactory(this.connectionFactoryMap.get("tokenCache"));
			}
			
			cache = abstractCache ;
			CACHES.put(name, cache) ; // 防止随后重复取出
		}
		return cache ;  
	}	
	public void setConnectionFactoryMap(Map<String, JedisConnectionFactory> connectionFactoryMap) {
		this.connectionFactoryMap = connectionFactoryMap;
	}
}
