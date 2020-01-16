package com.devopsapi.servicesimpl;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.devopsapi.services.StreamingService;

import reactor.core.publisher.Mono;

@Service
public class StreamingServiceImpl implements StreamingService {
	
	
	private WebClient webClient;
	
	@PostConstruct
	public void init() {
		webClient = WebClient
				.builder()
				.baseUrl("http://dummy.restapiexample.com/api/v1/employee/")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	} 

	@Override
	public Mono<String> webClientGet(String params) {
		return webClient.get().uri(params).retrieve().bodyToMono(String.class);
	}
	
	
	

}
