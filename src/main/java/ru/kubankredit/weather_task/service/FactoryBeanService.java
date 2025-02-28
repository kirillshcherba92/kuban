package ru.kubankredit.weather_task.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FactoryBeanService {

//    private final RestTemplate restTemplate;
//    private final Map<String, WeatherService> stringWeatherServiceMap;
//
//    public FactoryBeanService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//        stringWeatherServiceMap = inintMapApiService();
//    }
//
//    private Map<String, WeatherService> inintMapApiService() {
//        Map<String, WeatherService> stringWeatherServiceMap = new HashMap<>();
//        stringWeatherServiceMap.put("yandex", new YndexService(restTemplate));
//        return stringWeatherServiceMap;
//    }
//
//    public List<WeatherService> getService(String nameOfService) {
//        WeatherService weatherService = null;
//        weatherService = stringWeatherServiceMap.get(nameOfService);
//        if (weatherService == null) {
//            return new ArrayList<>(stringWeatherServiceMap.values());
//        }
//        return List.of(weatherService);
//    }
}
