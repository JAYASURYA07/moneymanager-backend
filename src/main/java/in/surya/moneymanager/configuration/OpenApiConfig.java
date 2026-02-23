
package in.surya.moneymanager.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Money Manager API")
                        .version("1.0.0")
                        .description("REST APIs for Money Manager Backend Application")
                        .contact(new Contact()
                                .name("Jayasurya R")
                                .email("your-email@gmail.com")
                        )
                );
    }
}