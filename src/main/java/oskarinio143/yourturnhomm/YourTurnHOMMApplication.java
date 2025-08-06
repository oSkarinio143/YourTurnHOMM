package oskarinio143.yourturnhomm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class YourTurnHOMMApplication {

	public static void main(String[] args) {
		SpringApplication.run(YourTurnHOMMApplication.class, args);
	}
}
