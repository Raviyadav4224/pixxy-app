package com.pixxy.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin("*")
public class PixxyApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixxyApiGatewayApplication.class, args);
	}

}
