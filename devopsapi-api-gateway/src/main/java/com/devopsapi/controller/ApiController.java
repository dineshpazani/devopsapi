package com.devopsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devopsapi.service.DsService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(path = "/test")
public class ApiController {
	
	@Autowired
	private DsService dsService;
	
	@GetMapping("/get")
	public String getProducts() {
		return dsService.getData();
	}

}
