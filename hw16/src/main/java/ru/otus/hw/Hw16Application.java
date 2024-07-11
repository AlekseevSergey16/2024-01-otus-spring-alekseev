package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hw16Application {

	public static void main(String[] args) {
		SpringApplication.run(Hw16Application.class, args);
		System.out.printf("Site: %n%s%n", "http://localhost:8080");
	}

}
