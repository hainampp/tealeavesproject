package com.example.tea_leaves_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TeaLeavesProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeaLeavesProjectApplication.class, args);
	}

}
