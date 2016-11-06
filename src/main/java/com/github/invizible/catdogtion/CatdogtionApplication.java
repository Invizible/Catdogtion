package com.github.invizible.catdogtion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@EntityScan(
	basePackageClasses = { CatdogtionApplication.class, Jsr310JpaConverters.class }
)
@SpringBootApplication
public class CatdogtionApplication {

	@Bean
	public Validator localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}

	public static void main(String[] args) {
		SpringApplication.run(CatdogtionApplication.class, args);
	}
}
