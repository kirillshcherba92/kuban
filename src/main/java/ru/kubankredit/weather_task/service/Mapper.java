package ru.kubankredit.weather_task.service;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface Mapper<T extends JsonNode, V> {
    V mapFrom(T t);
}
