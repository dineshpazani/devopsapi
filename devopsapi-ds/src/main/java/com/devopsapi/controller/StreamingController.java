package com.devopsapi.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.devopsapi.controller.Controller.MyClass;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class StreamingController {
	
	
	WebClient webClient = WebClient.create("http://dummy.restapiexample.com/api/v1/employees");
	
	
	
    @GetMapping(path = "/stream1", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<MyClass> stream() {
    	

    	
    	List<Integer> numberList = IntStream.rangeClosed(0, 10)
    		    .boxed().collect(Collectors.toList());
    	
    	 
    	
    	return  Flux
                .fromIterable(numberList)
                .window(5) // combine elements in batch of 5 (probably buffer will fit better, have a look)

           //     .delayElements(Duration.ofSeconds(3)) // for testing purpose you can use this function as well

                .flatMap(flow -> flow
                        // perform an external async call for each element in batch of 10
                        .parallel()
                        .runOn(Schedulers.parallel())

                        // single call
                        // .flatMap(element -> webClient.get().exchange())

                        // several calls
                        .flatMap(element -> Flux
                                .concat(IntStream
                                        .range(0, 5)
                                        .parallel()
                                        .mapToObj(i -> webClient.get().exchange())
                                        .collect(Collectors.toList())
                                )
                        )
                        .map(r -> r.bodyToMono(MyClass.class))
                )

                // subscribe to each response and throw received element further to the stream
                .flatMap(response -> Mono.create(s -> response.subscribe(s::success)))
                .doOnNext(r -> System.out.println("Response from first service {}"+ r))

                .window(2) // batch of 2 is ready
              //  .delayElements(Duration.ofSeconds(5)) // for testing purpose you can use this function as well

                .doOnNext(flow -> System.out.println("Batch of 2 is ready for second call")) // double check tells that batch is ready
                .flatMap(flow -> flow
                        .parallel()
                        .runOn(Schedulers.parallel())
                        .flatMap(element -> webClient.get().exchange())
                        .map(r -> r.bodyToMono(MyClass.class))
                )
                .flatMap(response -> Mono.create(s -> response.subscribe(s::success)));
    					
    }
	
	

}
