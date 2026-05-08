package com.example.viewerLimiterSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ViewerLimiterSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViewerLimiterSystemApplication.class, args);
	}

}
