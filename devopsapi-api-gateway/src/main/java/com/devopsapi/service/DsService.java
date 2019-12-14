package com.devopsapi.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.devopsapi.config.CustomFeignConfig;

@FeignClient(name="DEVOPS-DATASERVICE")
public interface DsService {
	
	@GetMapping("/test/get")
	public String getData();

}
