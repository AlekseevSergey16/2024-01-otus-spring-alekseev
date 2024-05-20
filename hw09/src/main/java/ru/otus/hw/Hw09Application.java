package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hw09Application {

	public static void main(String[] args) {
		SpringApplication.run(Hw09Application.class, args);
		System.out.printf("Чтобы перейти на страницу сайта открывай: %n%s%n", "http://localhost:8080");
	}

}
