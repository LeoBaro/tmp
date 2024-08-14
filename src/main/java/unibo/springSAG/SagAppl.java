package unibo.springSAG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class SagAppl {

	public static void main(String[] args) {

		Properties properties = System.getProperties();
		System.out.println("________________Properties: " + properties.getProperty("activeProfile"));

		SpringApplication.run(SagAppl.class, args);
	}

}
