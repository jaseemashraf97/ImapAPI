package com.example.pieEye.emailRetriever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.example.pieEye.emailRetriever.Controller")
public class EmailRetrieverApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EmailRetrieverApplication.class, args);
	}

}
