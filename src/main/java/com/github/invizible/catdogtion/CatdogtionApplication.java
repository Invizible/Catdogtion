package com.github.invizible.catdogtion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@EnableScheduling
@EnableAsync
@EntityScan(
	basePackageClasses = { CatdogtionApplication.class, Jsr310JpaConverters.class }
)
@SpringBootApplication
public class CatdogtionApplication {

  @Value("${schedulerPoolSize}")
  private int schedulerPoolSize;

  @Bean
	public Validator localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(schedulerPoolSize);
    return scheduler;
  }

	public static void main(String[] args) {
		SpringApplication.run(CatdogtionApplication.class, args);
	}
}
