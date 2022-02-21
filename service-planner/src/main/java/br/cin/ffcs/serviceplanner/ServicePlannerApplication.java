package br.cin.ffcs.serviceplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServicePlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicePlannerApplication.class, args);
	}

}