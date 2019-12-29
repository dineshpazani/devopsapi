package com.devopsapi.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

public class Test {
	

	public static void main(String[] args) {
		
		List<String> ids = new ArrayList<>();
		Random rand = new Random(); 

		System.out.println("Started..........");
		for(int i=0; i<20; i++) {
			ids.add(Integer.toString(rand.nextInt(1000000)));
		}
		
			
		getStrings(ids);//.forEach(System.out::println);
		System.out.println("End........");

	}

	public static List<String> getStrings(List<String> ids) {

		List<CompletableFuture<String>> futures = ids.stream().map(id -> getStringAsync(id))
				.collect(Collectors.toList());

		List<String> result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

		return result;
	}

	static CompletableFuture<String> getStringAsync(String id) {

		RestTemplate restTemplate = new RestTemplate();
		System.out.println("Reg id "+id);

		CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
			public String get() {
				final String String = restTemplate.getForObject("http://dummy.restapiexample.com/api/v1/employee/"+id,
						String.class);

				return String;
			}
		});

		return future;
	}

}
