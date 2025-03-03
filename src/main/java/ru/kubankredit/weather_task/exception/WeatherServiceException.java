package ru.kubankredit.weather_task.exception;

public class WeatherServiceException extends RuntimeException{

    private String serviceName;

    public WeatherServiceException() {
        super();
    }

    public WeatherServiceException(String message, String serviceName) {
        super(message);
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
