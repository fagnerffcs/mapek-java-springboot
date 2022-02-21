package br.cin.ffcs.serviceexecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceExecutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceExecutorApplication.class, args);
	}

}
