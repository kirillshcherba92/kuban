package ru.kubankredit.weather_task.controller;

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
            weatherResponseModels.add(weatherService.getCurrentWeather(cityName));
        });
        return weatherResponseModels;
    }

    @GetMapping("week")
    public List<List<WeatherResponseModel>> getWeekWeather(@Nullable @RequestParam("city") String cityName) {
        List<List<WeatherResponseModel>> weatherResponseModels = new ArrayList<>();
        weatherServices.forEach(weatherService -> {
            weatherResponseModels.add(weatherService.getWeekWeather(cityName));
        });
        return weatherResponseModels;
    }

    @GetMapping("geo")
    public PointPos getPint(@Nullable @RequestParam("city") String cityName) {
        return geoService.getPoint(cityName);
    }
}
