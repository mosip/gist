package io.mosip.partner.partnermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {})
public class PartnermanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnermanagementApplication.class, args);
	}

}
