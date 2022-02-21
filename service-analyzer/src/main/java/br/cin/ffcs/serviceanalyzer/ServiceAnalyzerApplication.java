package br.cin.ffcs.serviceanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAnalyzerApplication.class, args);
	}

}
