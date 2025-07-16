package oskarinio143.heroes3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.SecureRandom;
import java.util.Base64;

@EnableScheduling
@SpringBootApplication
public class Heroes3Application {

	public static void main(String[] args) {
		SpringApplication.run(Heroes3Application.class, args);
	}
}
