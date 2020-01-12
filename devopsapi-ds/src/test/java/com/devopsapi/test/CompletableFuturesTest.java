package com.devopsapi.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;
public class CompletableFuturesTest {

	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newFixedThreadPool(1000);
		RestTemplate restTemplate = new RestTemplate();
		
		List<CompletableFuture> list = new ArrayList<>();
		
		AtomicInteger ai = new AtomicInteger(1);
		
		for(int i=0;i<100000;i++) {
			CompletableFuture<Object> cff = null;

				cff = CompletableFuture.supplyAsync(() -> {
					return ai.getAndAdd(1)+" first list";
				}).thenApplyAsync(v -> {
					
					Random r = new Random();
					Integer in = r.nextInt(1000);
					
				/*
				 * try {
				 * 
				 * Thread.sleep(in); } catch (InterruptedException e) { e.printStackTrace(); }
				 */
					return v+" second values "+in+" "+restTemplate.getForObject("http://dummy.restapiexample.com/api/v1/employee/"+ai.get(), String.class) ;
				}, executor);

				list.add(cff);

			
		}
		
		// To get all
		//List<String> s = list.stream().map(f -> f.join().toString()).collect(Collectors.toList());
		
		List<String> s = list.stream().map(f -> {
			try {
				System.out.println(f.get());
				return f.get().toString();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		} ).collect(Collectors.toList());
		
		s.forEach(v -> System.out.println(v));
		
		
		
		/*
		 * list.stream().map(f -> { try { return f.get(); } catch (InterruptedException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (ExecutionException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * return ""; }).collect(Collectors.toList()).forEach(f ->
		 * System.out.println(f));
		 */
		
		executor.shutdown();

		
	

	}

}

class RestCall {

	public String run() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "retunr string";
	}

}
