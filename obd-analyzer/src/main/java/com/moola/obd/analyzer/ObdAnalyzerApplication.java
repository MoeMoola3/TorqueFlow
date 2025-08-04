package com.moola.obd.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ObdAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObdAnalyzerApplication.class, args);
	}

}
