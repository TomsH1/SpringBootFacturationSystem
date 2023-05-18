package com.backend.facturationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
@RequestMapping("/get")
public class FacturationsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacturationsystemApplication.class, args);
	}

	@GetMapping(value="/index")
	@ResponseBody
	public String getIndex(){
		return "<h1>Bienvenido! 🌐 </h1>";
	}
}
