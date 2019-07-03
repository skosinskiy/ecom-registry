package com.kosinskyi.ecom.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApplicationStarter {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationStarter.class, args);
  }

}
