package com.example.autoboard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.autoboard.service.DataSeeder;

@SpringBootApplication
public class AutoboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoboardApplication.class, args);
	}
}
