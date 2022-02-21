package br.cin.ffcs.servicemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceMonitorApplication.class, args);
	}

}
