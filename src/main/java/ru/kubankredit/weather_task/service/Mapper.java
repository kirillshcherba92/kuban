package ru.kubankredit.weather_task.service;

import com.fasterxml.jackson.databind.JsonNode;
import ru.kubankredit.weather_task.model.WeatherResponseModel;

@FunctionalInterface
public interface Mapper<T extends JsonNode, V> {
    V mapFrom(T t);
}
