package com.bestarch.demo.jedis.operations;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;

public interface BasicRedisOperations {
	
	public Logger getLogger();
	
	public RedisTemplate<String, String> getRedisTemplate();
	
	public void testRedisOperations();
	
	public void testHashInRedisTemplate();
	
	public void testStringInRedisTemplate();
	
	public void test();
	
	default public void checkSetAndGet() throws IOException {

	}

}
