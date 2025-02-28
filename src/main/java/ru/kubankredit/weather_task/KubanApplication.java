package ru.kubankredit.weather_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.kubankredit.weather_task.service.AuthTokenProperties;

@SpringBootApplication
public class KubanApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubanApplication.class, args);
    }

}
