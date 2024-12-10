package com.bestarch.demo.jedis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.MultiClusterClientConfig;
import redis.clients.jedis.MultiClusterClientConfig.ClusterConfig;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.providers.MultiClusterPooledConnectionProvider;

@Configuration
public class RedisConfig_Simple {

	/**
	 * Redis Configuration for cluster 1
	 */
	@Value("${redis.url:localhost}")
	private String url;

	@Value("${redis.port:6379}")
	private Integer port;

	@Value("${redis.password:admin}")
	private String password;
	
	
	/**
	 * Redis Configuration for cluster 2
	 * (Most of the time this would be the A-A counterpart of above cluster)
	 */
	@Value("${redis.alternate.url:localhost}")
	private String alternateUrl;

	@Value("${redis.alternate.port:6379}")
	private Integer alternatePort;

	@Value("${redis.alternate.password:admin}")
	private String alternatePassword;

/*	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(url, port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
		return redisConnectionFactory;
	}
*/	
	
	@Bean
	public MultiClusterPooledConnectionProvider multiClusterPooledConnectionProvider() {
		JedisClientConfig config = DefaultJedisClientConfig.builder().password(password).build();
		JedisClientConfig alternateConfig = DefaultJedisClientConfig.builder().password(alternatePassword).build();
		
		ClusterConfig[] clientConfigs = new ClusterConfig[2];
		clientConfigs[0] = new ClusterConfig(new HostAndPort(url, port), config);
		clientConfigs[1] = new ClusterConfig(new HostAndPort(alternateUrl, alternatePort), alternateConfig);
		
		MultiClusterClientConfig.Builder builder = new MultiClusterClientConfig.Builder(clientConfigs);
		builder.circuitBreakerSlidingWindowSize(10);
		builder.circuitBreakerSlidingWindowMinCalls(1);
		builder.circuitBreakerFailureRateThreshold(50.0f);
		
		MultiClusterPooledConnectionProvider provider = new MultiClusterPooledConnectionProvider(builder.build());
		RedisFailoverAdvisor redisFailoverAdvisor = new RedisFailoverAdvisor();
		provider.setClusterFailoverPostProcessor(redisFailoverAdvisor);
		
		return provider;
	}
	
	@Bean
	public UnifiedJedis unifiedJedis(MultiClusterPooledConnectionProvider provider) {
		UnifiedJedis jedis = new UnifiedJedis(provider);
		return jedis;
	}
	

}
