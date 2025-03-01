package ru.kubankredit.weather_task.service.weather.gis;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import ru.kubankredit.weather_task.service.Services;

public class ConditionGisService implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String apiWeatherService = context.getEnvironment().getProperty("api_weather_service", String.class);
        if (apiWeatherService == null || apiWeatherService.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (Services.ALL.getName().equals(apiWeatherService) || Services.GIS.getName().equals(apiWeatherService)) {
            return true;
        }
        return false;
    }
}
