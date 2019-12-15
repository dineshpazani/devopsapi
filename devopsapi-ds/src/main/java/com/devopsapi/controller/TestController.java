package com.devopsapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(path = "/v1")
public class TestController {
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String getProducts() {
		return "test";
	}

}
