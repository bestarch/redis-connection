package com.bestarch.demo.jedis.operations.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.bestarch.demo.jedis.operations.BasicRedisOperations;

import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.providers.MultiClusterPooledConnectionProvider;
import redis.clients.jedis.util.KeyValue;

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
				"item".getBytes(), "apple".getBytes(), 
				"colour".getBytes(),"red".getBytes(), 
				"weight".getBytes(), "10kg".getBytes());
		jedis.hset("fruit".getBytes(), map);
		IntStream.rangeClosed(1, 10).forEach(i -> {
			jedis.set("key"+i, "val"+i);
		});
		
		Map<byte[], byte[]> mapFetchedFromRedis = jedis.hgetAll("fruit".getBytes());
		KeyValue<Long, Long> res = jedis.waitAOF("key6", 0l, 1l, 100l);
		logger.info(res.toString());
		logger.info("Map fetched from Redis:: {}", mapFetchedFromRedis);
	}
	
	
	public List<String> getValues() {
		List<String> vals = new ArrayList<>();
		IntStream.rangeClosed(1, 10).forEach(i -> {
			String val = jedis.get("key"+i);
			vals.add(val);
		});
		Map<String, String> map = jedis.hgetAll("fruit");
		vals.addAll(map.values());
		return vals;
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
