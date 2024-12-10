package com.bestarch.demo.jedis.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestarch.demo.jedis.operations.impl.BasicRedisOperationsImpl_UnifiedJedis;

@RestController
@RequestMapping("/demo")
public class DemoWebController {
	
	@Autowired
	BasicRedisOperationsImpl_UnifiedJedis basicRedisOperations;

	@PostMapping("/perform_ops")
	public ResponseEntity<?> performOps() {
		basicRedisOperations.testRedisOperations();
		return ResponseEntity.accepted().build();
	}
	
	@PostMapping("/reset_redis_failure")
	public ResponseEntity<?> resetRedisFailure() {
		basicRedisOperations.resetRedisFailure();
		return ResponseEntity.accepted().build();
	}

}