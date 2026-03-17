package com.bestarch.demo.lettuce.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig_Simple {

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
		
		// Pool config
        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(10);       // max connections
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(2);
        
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.defaultConfiguration();
        
        // If you don't want default settings, use the following code to customise the behavior. 
        // LettuceClientConfiguration clientConfig =
        //        LettucePoolingClientConfiguration.builder()
        //                .commandTimeout(Duration.ofSeconds(5))   // command timeout
        //                .shutdownTimeout(Duration.ZERO)
        //                .poolConfig(poolConfig)                  // enable pooling
        //                .build();
		
		RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
		return redisConnectionFactory;
	}

	@Bean
	RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setDefaultSerializer(StringRedisSerializer.UTF_8);
		template.setConnectionFactory(connectionFactory);
		template.afterPropertiesSet();
		return template;
	}

}
