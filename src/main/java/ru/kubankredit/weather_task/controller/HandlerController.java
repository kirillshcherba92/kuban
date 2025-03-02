package ru.kubankredit.weather_task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kubankredit.weather_task.exception.GisWeatherServiceException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerController {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleControllerException(GisWeatherServiceException weatherServiceException) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(weatherServiceException.getMessage());
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Сервис", weatherServiceException.getServiceName());
        responseBody.put("ERROR", weatherServiceException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseBody);
    }
}
