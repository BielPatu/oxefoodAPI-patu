package br.com.ifpe.oxefood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OxefoodAPIpatuApplication {

	public static void main(String[] args) {
		SpringApplication.run(OxefoodAPIpatuApplication.class, args);
	}

}
