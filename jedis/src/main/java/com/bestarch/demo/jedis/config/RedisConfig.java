package com.bestarch.demo.jedis.config;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	@Value("${redis.url:localhost}")
	private String url;

	@Value("${redis.port:6379}")
	private Integer port;

	@Value("${redis.password:admin}")
	private String password;
	
	@Value("${redis.ssl.enabled:false}")
	private String sslEnabled;
	
	
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(url, port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		
		JedisClientConfiguration configuration = null;
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		String clientName = "my-client";
		
		if (Boolean.valueOf(sslEnabled)) {
			SSLParameters sslParameters = new SSLParameters();
			SSLContext context;
			try {
				context = SSLContext.getDefault();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			SSLSocketFactory socketFactory = context.getSocketFactory();
			
			configuration = JedisClientConfiguration.builder().useSsl()
					.hostnameVerifier(MyHostnameVerifier.INSTANCE) 
					.sslParameters(sslParameters) 
					.sslSocketFactory(socketFactory).and() 
					.clientName(clientName) 
					.connectTimeout(Duration.ofSeconds(10))
					.readTimeout(Duration.ofSeconds(10))
					.usePooling().poolConfig(poolConfig) 
					.build();
		}

		configuration = JedisClientConfiguration.builder()
				.clientName(clientName)
				.connectTimeout(Duration.ofSeconds(10))
				.readTimeout(Duration.ofSeconds(10))
				.usePooling().poolConfig(poolConfig).build();
		
		
		RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, configuration);
		return redisConnectionFactory;
	}
	
	
	enum MyHostnameVerifier implements HostnameVerifier {

		INSTANCE;

		@Override
		public boolean verify(String s, SSLSession sslSession) {
			return false;
		}
	}
	

}
