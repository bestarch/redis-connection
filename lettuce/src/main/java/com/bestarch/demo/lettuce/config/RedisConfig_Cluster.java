package com.bestarch.demo.lettuce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;

//@Configuration
public class RedisConfig_Cluster {
	
	@Value("${redis.url:localhost}")
	private String url;
	
	@Value("${redis.port:6379}")
	private Integer port;
	
	@Value("${redis.password:admin}")
	private String password;
	
	@Value("${redis.ssl.enabled:false}")
	private Boolean sslEnabled;
	
	
	@Bean
	public RedisClusterClient redisClusterClient() {
		
		RedisURI redisUri = RedisURI.Builder.redis(url)
	              .withPort(port)
	              .withPassword(password.toCharArray())
	              .build();

		RedisClusterClient clusterClient = RedisClusterClient.create(redisUri);
        return clusterClient;
	}
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(url, port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
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
