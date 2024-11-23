package authen_services;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "AuthenApis", version = "1.0", description = "AuthenApis"))
public class AuthenServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenServicesApplication.class, args);
	}

}
