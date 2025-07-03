package com.bestarch.demo.jedis.operations;

import java.io.IOException;

public interface BasicRedisOperations {
	
	public void testRedisOperations();
	
	default public void checkSetAndGet() throws IOException {

	}

}
