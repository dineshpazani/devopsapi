package com.devopsapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class TestController {
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String getProducts() {
		return "test";
	}

}
