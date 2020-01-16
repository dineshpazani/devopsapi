package com.devopsapi.services;

import reactor.core.publisher.Mono;

public interface StreamingService {
	
	
	public Mono<String> webClientGet(String params);

}
