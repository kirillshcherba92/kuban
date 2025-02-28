package ru.kubankredit.weather_task.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfigure {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
