package com.bestarch.demo.jedis.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestarch.demo.jedis.operations.BasicRedisOperations;
import com.bestarch.demo.jedis.operations.impl.MultiClusterRedisOperations;

@RestController
@RequestMapping("/demo")
public class DemoWebController {
	
	@Autowired
	BasicRedisOperations basicRedisOperations;

	@PostMapping("/test")
	public ResponseEntity<?> addKeys() {
		basicRedisOperations.testRedisOperations();
		return ResponseEntity.accepted().build();
	}
	
	
	@PostMapping("/reset")
	public ResponseEntity<?> resetRedisFailure() {
		((MultiClusterRedisOperations)basicRedisOperations).resetRedisFailure();
		return ResponseEntity.accepted().build();
	}

}