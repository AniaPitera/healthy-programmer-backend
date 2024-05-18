package pl.healthyprogrammer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@OpenAPIDefinition(info = @Info( title = "HealthyProgrammer API", version = "v1", description = "API with Spring Boot 3" ))
public class HealthyProgrammerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthyProgrammerApplication.class, args);
    }

}
