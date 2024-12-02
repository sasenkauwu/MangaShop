package com.SemestralnaPraca.MangaShop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
@SpringBootApplication
public class MangaShopApplication {


	public static void main(String[] args) {
		SpringApplication.run(MangaShopApplication.class, args);
	}
}
