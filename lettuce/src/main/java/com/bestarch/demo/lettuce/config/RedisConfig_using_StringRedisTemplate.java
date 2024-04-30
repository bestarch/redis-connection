package com.bestarch.demo.lettuce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

//@Configuration
public class RedisConfig_using_StringRedisTemplate {
	
	@Value("${redis.url:localhost}")
	private String url;
	
	@Value("${redis.port:6379}")
	private Integer port;
	
	@Value("${redis.password:admin}")
	private String password;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(url, port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		return redisConnectionFactory;
	}
	
	/**
	 * Use this method when only String operations are required. 
	 * 'StringRedisTemplate' also covers for RedisTemplate<String, String> in which case a 
	 * separate configuration method for RedisTemplate<String, String> is not required. 
	 */
	
	@Bean
	StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(connectionFactory);
		return template;
	}
	

}
