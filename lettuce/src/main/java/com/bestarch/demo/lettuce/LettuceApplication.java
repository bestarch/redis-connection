package com.bestarch.demo.lettuce;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import com.bestarch.demo.lettuce.operations.BasicRedisOperations;

@SpringBootApplication
public class LettuceApplication implements CommandLineRunner {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BasicRedisOperations basicRedisOperations;
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(LettuceApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		basicRedisOperations.testRedisOperations();
		basicRedisOperations.testHashInRedisTemplate();
		basicRedisOperations.testStringInRedisTemplate();
		basicRedisOperations.checkSetAndGet();
		//basicRedisOperations.testRedisCluster();
		//name();
	}
	
	
	public void name() {
		String file = "/Users/abhishek/apps/Work/test/Big_file/random_text_6_9MB.txt";
		Path filePath = Path.of(file);
		try {
			String data = Files.readString(filePath);
			redisTemplate.opsForValue().set(data, "hi");
			logger.info(redisTemplate.opsForValue().get(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
