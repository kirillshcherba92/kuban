package ru.kubankredit.weather_task.exception;

import ru.kubankredit.weather_task.service.Services;

public class GisWeatherServiceException extends RuntimeException{

    public GisWeatherServiceException() {
        super();
    }

    public GisWeatherServiceException(String message) {
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getServiceName() {
        return Services.GIS.getName();
    }
}
