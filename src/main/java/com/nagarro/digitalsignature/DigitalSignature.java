package com.nagarro.digitalsignature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DigitalSignature{

	public static void main(String[] args) {
		SpringApplication.run(DigitalSignature.class, args);
	}

}
