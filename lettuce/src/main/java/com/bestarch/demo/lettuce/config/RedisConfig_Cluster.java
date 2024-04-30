package com.bestarch.demo.lettuce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

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
	
	/**
	 * Use following code to do redis operations
	 */
	/*
		RedisClusterClient clusterClient = null; // Get this from Spring container
		StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
	    try {
	        // Synchronous Commands
	        RedisAdvancedClusterCommands<String, String> syncCommands = connection.sync();
	
	        // Example Command
	        syncCommands.set("key", "value");
	        String value = syncCommands.get("key");
	        System.out.println("Retrieved value: " + value);
	
	        // Further operations...
	    } finally {
	        // Properly close the connection and client
	        connection.close();
	        clusterClient.shutdown();
	    }
	*/
	

}
