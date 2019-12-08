package com.devopsapi.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@EnableHystrixDashboard
@EnableCircuitBreaker
@ComponentScan("com.devopsapi.*")
@EnableFeignClients(basePackages = {"com.devopsapi.service"})
public class DevopsApiGatewayApp {
	
	public static void main(String[] args) {
		SpringApplication.run(DevopsApiGatewayApp.class, args);
	}

}
