package com.devopsapi.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping(path = "/v1")
public class TestController {
	
	
	private final WebClient w = WebClient.create("http://dummy.restapiexample.com/api/v1/employee/1");


	@GetMapping(path = "/stream1", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<MyClass> getstream1() {
	    return Flux.range(1, 100)
	    			.window(10)
	    		//	.delayElements(Duration.ofSeconds(5))
	    			.doOnNext(flow -> System.out.println("Batch of 10 is ready "))
	    			.flatMap(e ->  w.get().exchange())
	    			.map(r -> r.bodyToMono(MyClass.class))	    		  
	    			.flatMap(response -> Mono.create(s -> response.subscribe(s::success)));   			

	}

	

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String getProducts() {
		return "test";
	}

	@GetMapping("/stream-sse")
	public Flux<ServerSentEvent> streamEvents() {
	    return Flux.interval(Duration.ofSeconds(1))
	      .map(sequence -> ServerSentEvent.<String> builder()
	        .id(String.valueOf(sequence))
	          .event("periodic-event")
	          .data("SSE - " + LocalTime.now().toString())
	          .build());
	}
	
	@GetMapping(path = "/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<String> getstream() {
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		
		List<CompletableFuture> list = new ArrayList<>();
		
		AtomicInteger ai = new AtomicInteger(1);
		RestTemplate restTemplate = new RestTemplate();
		
		for(int i=0;i<100;i++) {
			CompletableFuture<Object> cff = null;

				cff = CompletableFuture.supplyAsync(() -> {
					return ai.getAndAdd(1)+" first downstream web service "+restTemplate.getForObject("http://dummy.restapiexample.com/api/v1/employee/"+ai.get(), String.class) ;
				}).thenApplyAsync(v -> {
					
					Random r = new Random();
					Integer in = r.nextInt(1000);			
									
					return v+" second downstream web service  "+in+" "+restTemplate.getForObject("http://dummy.restapiexample.com/api/v1/employee/"+ai.get()+1, String.class) +" \n";
				}, executor);

				list.add(cff);

			
		}
		
		
		return Flux.fromStream(list.stream().map(m -> {
			try {
				
				return m.get().toString();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return "";
		}));
		
	}
	
	@GetMapping(path = "/test", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Integer> getusers() {
		
		return Flux.range(1, 100)
				.delayElements(Duration.ofSeconds(1))
				.log();
		
	}
	
	public  Stream<String> d1() {
		return   Stream.generate(() ->{
        	
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
            return Double.toString(Math.random() * 1000);
            }).limit(3);
	}
	
	public  Stream<String> d2() {
		return   Stream.generate(() ->{
        	
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
            return Double.toString(Math.random() * 1000);
            }).limit(3);
	}
	
	public Flux<String> m1(){
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("m1 ");
		return Flux.just(restTemplate.getForObject("http://dummy.restapiexample.com/api/v1/employees", String.class));
	}
	
	public Flux<String> m2(){
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("m2 ");
		return Flux.just(restTemplate.getForObject("http://dummy.restapiexample.com/api/v1/employee/1", String.class));
	}
	
	
}

class MyClass {
    public Integer i;
}
