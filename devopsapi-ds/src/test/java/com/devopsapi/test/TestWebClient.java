package com.devopsapi.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public class TestWebClient {
	

	public static void main(String[] args) {
		
		List<String> ids = new ArrayList<>();
		Random rand = new Random(); 

		System.out.println("Started..........");
		for(int i=0; i<1000000; i++) {
			ids.add(Integer.toString(rand.nextInt(1000000)));
		}
		
			
		getStrings(ids);//.forEach(System.out::println);
		System.out.println("End........");

	}

	public static List<String> getStrings(List<String> ids) {
		
		

		List<CompletableFuture<String>> futures = ids.stream().map(id -> {
		return getStringAsync(id, 0);
		})
				.collect(Collectors.toList());

		List<String> result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

		return result;
	}

	static CompletableFuture<String> getStringAsync(String id, Integer i) {

		System.out.println("Reg id "+id+" "+i);

		CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
			public String get() {
				
				
				WebClient.builder()
		        .baseUrl("http://dummy.restapiexample.com/api/v1/employee/"+id)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
		        .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
		        .build()
		        .get().exchange();
				
				return "Ok";
			}
		});

		return future;
	}

}
