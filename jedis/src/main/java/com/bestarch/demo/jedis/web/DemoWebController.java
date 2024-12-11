package com.bestarch.demo.jedis.web;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestarch.demo.jedis.operations.impl.BasicRedisOperationsImpl_UnifiedJedis;

@RestController
@RequestMapping("/demo")
public class DemoWebController {
	
	@Autowired
	BasicRedisOperationsImpl_UnifiedJedis basicRedisOperations;

	@PostMapping("/add")
	public ResponseEntity<?> addKeys() {
		basicRedisOperations.testRedisOperations();
		return ResponseEntity.accepted().build();
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getKeys() {
		List<String> vals = basicRedisOperations.getValues();
		return ResponseEntity.ok(vals);
	}
	
	@PostMapping("/reset")
	public ResponseEntity<?> resetRedisFailure() {
		basicRedisOperations.resetRedisFailure();
		return ResponseEntity.accepted().build();
	}

}