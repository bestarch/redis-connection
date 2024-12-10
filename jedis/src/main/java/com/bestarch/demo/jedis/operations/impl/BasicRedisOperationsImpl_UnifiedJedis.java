package com.bestarch.demo.jedis.operations.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.bestarch.demo.jedis.operations.BasicRedisOperations;

import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.providers.MultiClusterPooledConnectionProvider;

@Primary
@Component
public class BasicRedisOperationsImpl_UnifiedJedis implements BasicRedisOperations {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UnifiedJedis jedis;
	
	@Autowired
	MultiClusterPooledConnectionProvider multiClusterPooledConnectionProvider;
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	
	public void testRedisOperations() {
		Long size = jedis.dbSize();
		logger.info("DB Size:: {}", size);
		Map<byte[], byte[]> map = Map.of(
				"item".getBytes(), "orange".getBytes(), 
				"colour".getBytes(),"orange".getBytes(), 
				"weight".getBytes(), "10".getBytes());
		jedis.hset("fruit".getBytes(), map);
		Map<byte[], byte[]> mapFetchedFromRedis = jedis.hgetAll("fruit".getBytes());
		logger.info("Map fetched from Redis:: {}", mapFetchedFromRedis);
	}


	@Override
	public void testHashInRedisTemplate() {
		
	}


	@Override
	public void testStringInRedisTemplate() {
		
	}


	@Override
	public void test() {
		Long size = jedis.dbSize();
		logger.info("DB Size:: {}", size);
	}


	@Override
	public RedisTemplate<String, String> getRedisTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetRedisFailure() {
		multiClusterPooledConnectionProvider.setActiveMultiClusterIndex(1);
	}

}
