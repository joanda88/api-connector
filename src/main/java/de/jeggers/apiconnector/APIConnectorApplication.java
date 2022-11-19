package de.jeggers.apiconnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class APIConnectorApplication {

    private static final Logger LOG = LoggerFactory.getLogger(APIConnectorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(APIConnectorApplication.class, args);
	}
}
