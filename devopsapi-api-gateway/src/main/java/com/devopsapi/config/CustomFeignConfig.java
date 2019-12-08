package com.devopsapi.config;

import org.springframework.context.annotation.Bean;

import feign.Contract;
import feign.auth.BasicAuthRequestInterceptor;

public class CustomFeignConfig {
	

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }
}
