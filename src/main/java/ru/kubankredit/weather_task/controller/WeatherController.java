package ru.kubankredit.weather_task.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kubankredit.weather_task.model.PointPos;
import ru.kubankredit.weather_task.model.WeatherResponseModel;
import ru.kubankredit.weather_task.service.WeatherService;
import ru.kubankredit.weather_task.service.geo.GeoService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/weather/")
public class WeatherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);

    private final GeoService<String> geoService;
    private final List<WeatherService> weatherServices;

    public WeatherController(GeoService<String> geoService, List<WeatherService> weatherServices) {
        this.geoService = geoService;
        this.weatherServices = weatherServices;
    }

    @GetMapping("current")
    public List<WeatherResponseModel> getCurrentWeather(@Nullable @RequestParam("city") String cityName) {
        List<WeatherResponseModel> weatherResponseModels = new ArrayList<>();
        weatherServices.forEach(weatherService -> {
            LOGGER.info("RestIn \"api/v1/weather/current\". Вызов контроллера для получения текущей погоды. Сервис: {}", weatherService.getName());
            weatherResponseModels.add(weatherService.getCurrentWeather(cityName));
        });
        return weatherResponseModels;
    }

    @GetMapping("week")
    public List<List<WeatherResponseModel>> getWeekWeather(@Nullable @RequestParam("city") String cityName) {
        List<List<WeatherResponseModel>> weatherResponseModels = new ArrayList<>();
        weatherServices.forEach(weatherService -> {
            LOGGER.info("RestIn \"api/v1/weather/week\". Вызов контроллера для получения погоды за неделю. Сервис: {}", weatherService.getName());
            weatherResponseModels.add(weatherService.getWeekWeather(cityName));
        });
        return weatherResponseModels;
    }

    @GetMapping("geo")
    public PointPos getPint(@Nullable @RequestParam("city") String cityName) {
        LOGGER.info("RestIn \"api/v1/weather/geo\". Вызов контроллера для получения геолокации.");
        return geoService.getPoint(cityName);
    }
}
