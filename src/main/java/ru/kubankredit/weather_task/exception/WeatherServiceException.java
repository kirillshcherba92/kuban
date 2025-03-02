package ru.kubankredit.weather_task.exception;

import ru.kubankredit.weather_task.service.Services;

public class WeatherServiceException extends RuntimeException{

    private String serviceName;

    public WeatherServiceException() {
        super();
    }

    public WeatherServiceException(String message, String serviceName) {
        super(message);
        this.serviceName = serviceName;
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
