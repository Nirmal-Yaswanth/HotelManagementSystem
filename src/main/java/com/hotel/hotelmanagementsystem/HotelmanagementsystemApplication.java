package com.hotel.hotelmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // Import ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = "com.hotel.hotelmanagementsystem") // Explicitly tell Spring where to scan
public class HotelmanagementsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelmanagementsystemApplication.class, args);
	}

}
