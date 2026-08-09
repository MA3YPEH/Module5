package aston.module5.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationOpenAPI(){
        return  new OpenAPI().info(
                new Info().title("Notification Service API")
                        .version("1.0.0")
                        .description("Сервис отправки сообщений на почту")
        );
    }
}
