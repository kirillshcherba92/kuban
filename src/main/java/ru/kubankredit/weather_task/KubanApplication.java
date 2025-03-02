package ru.kubankredit.weather_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.kubankredit.weather_task.service.AuthTokenProperties;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class KubanApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubanApplication.class, args);
    }

}
