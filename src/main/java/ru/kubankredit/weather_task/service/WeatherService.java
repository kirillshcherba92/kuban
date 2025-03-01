package ru.kubankredit.weather_task.service;

import ru.kubankredit.weather_task.model.WeatherResponseModel;

import java.util.List;

public interface WeatherService {
    WeatherResponseModel getCurrentWeather(String cityName);

    List<WeatherResponseModel> getWeekWeather(String cityName);
}
