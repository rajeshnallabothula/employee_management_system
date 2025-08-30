package com.brinta.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(basePackages = "com.brinta.ems")
public class employeeManagementJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(employeeManagementJavaApplication.class, args);
	}

}
