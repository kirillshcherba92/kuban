package ru.kubankredit.weather_task.service.yandex;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ConditionYandexService implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String apiWeatherService = context.getEnvironment().getProperty("api_weather_service", String.class);
        if (apiWeatherService == null || apiWeatherService.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if("all".equals(apiWeatherService) || "yandex".equals(apiWeatherService)) {
            return true;
        }
        return false;
    }
}
