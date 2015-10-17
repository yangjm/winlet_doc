package com.aggrepoint.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;

@SpringBootApplication
public class DocApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(DocApplication.class, args);
	}

	@Bean
	public ConfigurableWebBindingInitializer getWebBindingInitializer() {
		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		return initializer;
	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(DocApplication.class);
	}
}
