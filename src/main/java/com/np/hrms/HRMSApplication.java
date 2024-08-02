package com.np.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HRMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(HRMSApplication.class, args);
	}

}
