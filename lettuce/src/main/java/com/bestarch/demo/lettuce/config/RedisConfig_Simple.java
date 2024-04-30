package com.bestarch.demo.lettuce.config;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.protocol.ProtocolVersion;

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
	
	/**
	 * Open Redis connection using RedisClient
	 * @return
	 * @throws FileNotFoundException
	 */
	
//	@Bean
//	public RedisClient redisClient() throws FileNotFoundException {
//		
//		RedisURI redisUri = RedisURI.Builder.redis(url)
//                .withPort(port)
//                .withPassword(password.toCharArray())
//                .build();
//		RedisClient redisClient = RedisClient.create(redisUri);
//		
//		ClientOptions clientOptions = ClientOptions.builder()
//				.protocolVersion(ProtocolVersion.RESP3)
//				.build();
//		redisClient.setOptions(clientOptions);
//		
//			/* [Important]
//			 *
//			 * Following code snippet shows how to invoke Redis commands using StatefulRedisConnection
//			 *
//			 * StatefulRedisConnection<String, String> connection = redisClient.connect();
//			 * RedisCommands<String, String> syncCommands = connection.sync();
//			 * syncCommands.set("Hello", "World!!!"); 
//			 * connection.close();
//			 * redisClient.shutdown();
//			 */
//		 
//		return redisClient;
//	}

}
