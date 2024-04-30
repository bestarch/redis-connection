package com.bestarch.demo.lettuce.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ResourceUtils;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SslOptions;
import io.lettuce.core.protocol.ProtocolVersion;

//@Configuration
public class RedisConfig_SSL {
	
	@Value("${redis.url:localhost}")
	private String url;
	
	@Value("${redis.port:6379}")
	private Integer port;
	
	@Value("${redis.password:admin}")
	private String password;
	
	private Boolean sslEnabled = true;
	
	
	/**
	 * Open Redis connection using RedisConnectionFactory
	 * @return
	 * @throws FileNotFoundException
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactoryWithSSL() throws FileNotFoundException {

		/*
		 * Following lines of code configures SSL and timeout 
		 * */
		LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();

		if (sslEnabled) {
			File file = ResourceUtils.getFile("classpath:redis_ca.pem");
			SslOptions sslOptions = SslOptions.builder().trustManager(file).build();

			ClientOptions clientOptions = ClientOptions.builder()
					.sslOptions(sslOptions)
					.protocolVersion(ProtocolVersion.RESP3)
					.build();

			lettuceClientConfigurationBuilder.clientOptions(clientOptions)
					.useSsl().disablePeerVerification().and()
					.commandTimeout(Duration.ofSeconds(2))
					.shutdownTimeout(Duration.ZERO);
		}
		LettuceClientConfiguration lettuceClientConfiguration = lettuceClientConfigurationBuilder.build();

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(url, port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration,
				lettuceClientConfiguration);
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
	 * Open Redis connection using RedisClient with SSL
	 * @return
	 * @throws FileNotFoundException
	 */
//	
//	@Bean
//	public RedisClient redisClientWithSSL() throws FileNotFoundException {
//		
//		RedisURI redisUri = RedisURI.Builder.redis(url)
//                .withSsl(true)
//                .withPort(port)
//                .withPassword(password.toCharArray())
//                .build();
//		RedisClient redisClient = RedisClient.create(redisUri);
//		
//		/*
//		 * Following lines of code configures SSL and timeout 
//		 * */
//		File file = ResourceUtils.getFile("classpath:redis_ca.pem");
//		SslOptions sslOptions = SslOptions.builder().trustManager(file).build();
//
//		ClientOptions clientOptions = ClientOptions.builder()
//				.sslOptions(sslOptions)
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
//			 * syncCommands.set("Hello", "World!!!"); connection.close();
//			 * redisClient.shutdown();
//			 */
//		 
//		return redisClient;
//	}
	
	
}
