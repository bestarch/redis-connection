package com.bestarch.demo.jedis.config;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Connection;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.DefaultJedisClientConfig.Builder;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

//@Configuration
public class RedisConfig_UnifiedJedis {

	@Value("${redis.url:localhost}")
	private String url;

	@Value("${redis.port:6379}")
	private Integer port;

	@Value("${redis.password:admin}")
	private String password;
	
	@Value("${redis.ssl.enabled:false}")
	private String sslEnabled;
	
	@Value("${redis.ssl.truststore.path:}")
    private String truststorePath;
    
    @Value("${redis.ssl.truststore.password:}")
    private String truststorePassword;
	
	
	@Bean
	public UnifiedJedis unifiedJedis() {
		GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        
        // Create client configuration
        Builder clientConfigBuilder = DefaultJedisClientConfig.builder()
                .connectionTimeoutMillis(2000)
                .socketTimeoutMillis(2000);
        
        if (ObjectUtils.isEmpty(password)) {
            clientConfigBuilder.password(password);
        }
        
        // Configure SSL if enabled
        if (Boolean.valueOf(sslEnabled)) {
            clientConfigBuilder.ssl(true);
            
            // If custom truststore is provided
            if (truststorePath != null && !truststorePath.isEmpty()) {
                try {
                    SSLSocketFactory sslSocketFactory = createSSLSocketFactory();
                    clientConfigBuilder.sslSocketFactory(sslSocketFactory);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create SSL socket factory", e);
                }
            }
        }
        
        JedisClientConfig clientConfig = clientConfigBuilder.build();
        
        // Create connection provider with pool
        HostAndPort hostAndPort = new HostAndPort(url, port);
        JedisPooled pooled = new JedisPooled(poolConfig, hostAndPort, clientConfig);
        return pooled;
	}

	private SSLSocketFactory createSSLSocketFactory() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream trustStoreStream = new FileInputStream(truststorePath)) {
            trustStore.load(trustStoreStream, truststorePassword.toCharArray());
        }
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        return sslContext.getSocketFactory();
    }

}
