package com.bestarch.demo.lettuce.operations;

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
//		RedisTemplate<String, String> redisTemplate = getRedisTemplate();
//		byte[] encoded = Files.readAllBytes(Paths.get("/random_text_3MB.txt"));
//		String str = new String(encoded, "UTF-8");
//		redisTemplate.opsForValue().set("April1", "April2"); 
	}

}
