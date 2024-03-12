package ua.com.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Car Rest API", version = "v0.1"), servers = {
		@Server(description = "Local dev env", url = "http://localhost:8080") }
		)
public class OpenApiConfig {

}
