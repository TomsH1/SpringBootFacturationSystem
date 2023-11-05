package com.backend.facturationsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FacturationsystemApplication
implements CommandLineRunner
{
	@Value("${jwt_converted}")
	private String jwt_converted;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(FacturationsystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("this.passwordEncoder.encode(\"T3000520020\") = " + this.passwordEncoder.encode("T3000520020"));
	}
}
