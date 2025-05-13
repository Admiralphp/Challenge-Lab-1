package com.example.bookcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookcatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookcatalogApplication.class, args);
	}

}
