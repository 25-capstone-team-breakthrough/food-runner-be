package com.Hansung.Capston;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // env 변수를 등록하고 JPA를 사용하기 위한 어노테이션
@SpringBootApplication
public class CapstoneApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach((entry-> System.setProperty(entry.getKey(), entry.getValue())));
		SpringApplication.run(CapstoneApplication.class, args);
	}

}
