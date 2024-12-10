package com.bestarch.demo.lettuce.operations.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.bestarch.demo.lettuce.operations.BasicRedisOperations;

//@Component
public class BasicRedisOperations_StringRedisTemplateImpl implements BasicRedisOperations {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private RedisOperations<String, String> redisOperations;
	
	@Override
	public Logger getLogger() {
		return logger;
	}


	@Override
	public RedisTemplate<String, String> getRedisTemplate() {
		return stringRedisTemplate;
	}
	
	public void testRedisOperations() {

		redisOperations.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Long size = connection.serverCommands().dbSize();
				logger.info("DB Size:: {}", size);
				Map<byte[], byte[]> map = Map.of(
						"name".getBytes(), "india".getBytes(), 
						"states".getBytes(),"28".getBytes(), 
						"capital".getBytes(), "delhi".getBytes());
				
				connection.hashCommands().hMSet("country".getBytes(), map);
				// Can cast to StringRedisConnection if using a StringRedisTemplate
				return ((StringRedisConnection) connection).set("hello2", "world2");
			}
		});
	}


	@Override
	public void testHashInRedisTemplate() {
		Map<String, String> map = Map.of(
				"name", "Rahul", 
				"age","24", 
				"city", "Karnataka");
		getRedisTemplate().opsForHash().putAll("user:2", map);
		logger.info("Map fetched from Redis for key {}: {}", "user:2", getRedisTemplate().opsForHash().entries("user:2"));
	}


	@Override
	public void testStringInRedisTemplate() {
		getRedisTemplate().opsForValue().set("string2", "value2");
		logger.info("String value for {} from Redis: {}", "string2", getRedisTemplate().opsForValue().get("string2"));
	}


	@Override
	public void testRedisCluster() {
		// TODO Auto-generated method stub
		
	}

}
