package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hw09Application {

	public static void main(String[] args) {
		SpringApplication.run(Hw09Application.class, args);
		System.out.printf("Site: %n%s%n", "http://localhost:8080");
	}

}
