package cn.mldn.action;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.SerializationUtils;

@ContextConfiguration(locations = { "classpath:spring-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)

public class TestRedis {
	@Autowired
	private JedisConnectionFactory retryJedisConnectionFactory;

	@Test
	public void testConn() {
		AtomicInteger num = (AtomicInteger) SerializationUtils.deserialize(this.retryJedisConnectionFactory.getConnection().get(org.springframework.util.SerializationUtils.serialize("admin"))) ;
		System.out.println(num.intValue());
	}
}
