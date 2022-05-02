package br.com.safi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SafiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafiApplication.class, args);
	}

}
