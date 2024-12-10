package com.bestarch.demo.jedis.operations.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.bestarch.demo.jedis.operations.BasicRedisOperations;

import jakarta.annotation.Resource;

//@Component
public class BasicRedisOperationsImpl implements BasicRedisOperations {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	
	@Autowired
	@Resource(name="redisTemplate")
	private RedisOperations<String, String> redisOperations;
	

	
	@Override
	public Logger getLogger() {
		return logger;
	}


	@Override
	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}
	
	
	public void testRedisOperations() {
		redisOperations.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Long size = connection.serverCommands().dbSize();
				logger.info("DB Size:: {}", size);
				Map<byte[], byte[]> map = Map.of(
						"item".getBytes(), "orange".getBytes(), 
						"colour".getBytes(),"orange".getBytes(), 
						"weight".getBytes(), "10".getBytes());
				
				connection.hashCommands().hMSet("fruit".getBytes(), map);
				return connection.stringCommands().set("hello".getBytes(), "world".getBytes());
			}
		});
	}


	@Override
	public void testHashInRedisTemplate() {
		Map<String, String> map = Map.of(
				"name", "Sachin", 
				"age","22", 
				"city", "Mumbai");
		getRedisTemplate().opsForHash().putAll("user:1", map);
		logger.info("Map fetched from Redis for key {}: {}", "user:1", getRedisTemplate().opsForHash().entries("user:1"));
	}


	@Override
	public void testStringInRedisTemplate() {
		getRedisTemplate().opsForValue().set("string1", "value1");
		logger.info("String value for {} from Redis: {}", "string1", getRedisTemplate().opsForValue().get("string1"));
	}


	@Override
	public void test() {
		
		
	}


}
