package com.bestarch.demo.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bestarch.demo.jedis.operations.impl.BasicRedisOperationsImpl_UnifiedJedis;

@SpringBootApplication
public class JedisApplication {
	
	@Autowired
	BasicRedisOperationsImpl_UnifiedJedis basicRedisOperations;
	
	public static void main(String[] args) {
		SpringApplication.run(JedisApplication.class, args);
	}
	
}
