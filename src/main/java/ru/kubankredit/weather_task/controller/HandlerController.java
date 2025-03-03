package ru.kubankredit.weather_task.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kubankredit.weather_task.exception.WeatherServiceException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerController.class);

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleControllerGisException(final WeatherServiceException weatherServiceException) {
        return exceptionResponse(weatherServiceException);
    }

    private ResponseEntity<Map<String, String>> exceptionResponse(WeatherServiceException weatherServiceException) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Сервис", weatherServiceException.getServiceName());
        responseBody.put("ERROR", weatherServiceException.getMessage());

        LOGGER.error("REST CALL ERROR: {}", weatherServiceException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseBody);
    }
}
