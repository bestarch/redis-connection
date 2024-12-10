package com.bestarch.demo.jedis.config;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
public class RedisFailoverAdvisor implements Consumer<String> {

    @Override
    public void accept(String clusterName) {
        Logger logger = LoggerFactory.getLogger(RedisFailoverAdvisor.class);
        logger.warn("Redis is failing over to: " + clusterName);
    }
}