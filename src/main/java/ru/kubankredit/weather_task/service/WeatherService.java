package ru.kubankredit.weather_task.service;

import ru.kubankredit.weather_task.model.WeatherResponseModel;

import java.util.List;

public interface WeatherService {
    public WeatherResponseModel getCurrentWeather(String cityName);
    public List<WeatherResponseModel> getWeekWeather(String cityName);
}
