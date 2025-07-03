package com.bestarch.demo.jedis.operations.impl;

import java.util.Map;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bestarch.demo.jedis.operations.BasicRedisOperations;

import redis.clients.jedis.AbstractPipeline;
import redis.clients.jedis.CommandArguments;
import redis.clients.jedis.Protocol.Command;
import redis.clients.jedis.Response;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.providers.MultiClusterPooledConnectionProvider;

/**
 * Enable only when <link>RedisConfig_MultiClusterFailover.java</link> configuration is enabled
 */
//@Component
public class MultiClusterRedisOperations implements BasicRedisOperations {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UnifiedJedis jedis;
	
	@Autowired
	MultiClusterPooledConnectionProvider multiClusterPooledConnectionProvider;
	
	
	public void testRedisOperations() {
		Long size = jedis.dbSize();
		logger.info("DB Size before insertion:: {}", size);
		jedis.set("Hello", "23456789");
		Map<String, String> map = Map.of("key1","value1", "key2","value2", "key3","value3");
		jedis.hset("hashkey", map);
		logger.info("DB Size after insertion:: {}", map.size());
		//testWaitAoF();
	}
	
	/**
	 * Make sure AoF persistence is enabled, else an error will be thrown
	 */
	public void testWaitAoF() {
		Long size = jedis.dbSize();
		logger.info("DB Size before insertion:: {}", size);
		jedis.set("Hello", "23456789");
		Map<String, String> map = Map.of("key1","value1", "key2","value2", "key3","value3");
		jedis.hset("hashkey", map);
		logger.info("DB Size after insertion:: {}", map.size());
		
		logger.info("Inserting key and using WAITAOF");
		IntStream.rangeClosed(1, 50).forEach(i -> {
			AbstractPipeline pipeline = jedis.pipelined();
			pipeline.set("key"+i, "val"+i);
			

			CommandArguments waitAofCommand = new CommandArguments(Command.WAITAOF)
												.add(1) 
												.add(1)  // number of replicas
							                    .add(0); // Timeout
                    
			Response<Object> response = pipeline.sendCommand(waitAofCommand);
			pipeline.close();
			if(response != null) {
				Object res = response.get();
				logger.info("Response received from WAITAOF command: {}", res);
			}
		});
		logger.info("DB Size after insertion:: {}", jedis.dbSize());
		
		logger.info("Invoking WAITAOF command");
		
	}
	
	
	public void resetRedisFailure() {
		multiClusterPooledConnectionProvider.setActiveMultiClusterIndex(1);
	}

}
