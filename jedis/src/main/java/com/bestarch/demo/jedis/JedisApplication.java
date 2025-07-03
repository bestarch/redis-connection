package com.bestarch.demo.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bestarch.demo.jedis.operations.BasicRedisOperations;


@SpringBootApplication
public class JedisApplication implements CommandLineRunner{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BasicRedisOperations basicRedisOperations;
	
	public static void main(String[] args) {
		SpringApplication.run(JedisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Performing some test Redis commands");
		basicRedisOperations.testRedisOperations();
		
	}
	
}
