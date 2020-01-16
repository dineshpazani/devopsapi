package com.devopsapi.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@RestController
public class Controller {
	
	//https://stackoverflow.com/questions/59722242/how-to-return-response-immediate-to-client-in-spring-flux-by-controlling-the-no/59723426?noredirect=1#comment105626527_59723426

    private final WebClient webClient = WebClient.create("http://localhost:8081/call");

    private final AtomicInteger counter = new AtomicInteger(0);

    @GetMapping(path = "/streams", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<MyClass> stream() {
    	
    	List<Integer> numberList = IntStream.rangeClosed(0, 10)
    		    .boxed().collect(Collectors.toList());
        return Flux
        		 .fromIterable(numberList) // prepare initial 20 requests
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
	
    @GetMapping(path = "/call", produces = APPLICATION_JSON_VALUE)
    public Mono<MyClass> counter() {
        return Mono
                .just(new MyClass(counter.incrementAndGet()));
             //   .delayElement(Duration.ofSeconds(1)); // emulate response time
    }
	 


    public static class MyClass {
        public Integer i;

		public Integer getI() {
			return i;
		}

		public void setI(Integer i) {
			this.i = i;
		}

		public MyClass(Integer i) {
			super();
			this.i = i;
		}

		public MyClass() {
			super();
		}
		
		
        
        
    }

}