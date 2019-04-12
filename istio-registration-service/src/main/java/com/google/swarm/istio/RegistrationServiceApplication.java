package com.google.swarm.istio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.data.spanner.core.SpannerOperations;


@SpringBootApplication
public class RegistrationServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceApplication.class);

	@Autowired
	private SpannerOperations spannerOperations;

	
	public static void main(String[] args) {
		SpringApplication.run(RegistrationServiceApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner() {
//		return args -> {
//			this.spannerOperations.delete(Registration.class, KeySet.all());
//			Registration student = new Registration("1", "1001", "CSC100");
//			this.spannerOperations.insert(student);
//			logger.info("Test Record Inserted Successfully for Student Id: 1001 ");
//			
//		};
//	}
}
