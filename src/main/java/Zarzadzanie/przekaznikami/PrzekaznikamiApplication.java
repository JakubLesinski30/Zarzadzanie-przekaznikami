package Zarzadzanie.przekaznikami;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PrzekaznikamiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrzekaznikamiApplication.class, args);
	}

}
