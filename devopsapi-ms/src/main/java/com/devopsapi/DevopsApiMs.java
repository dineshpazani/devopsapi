package com.devopsapi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DevopsApiMs {
	
	public static void main(String[] args) {
		SpringApplication.run(DevopsApiMs.class, args);
	}

}
