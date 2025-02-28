package ru.kubankredit.weather_task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;
import ru.kubankredit.weather_task.model.WeatherResponseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapperFactory {
    private final String weatherGisApiSource = Services.GIS.getName();

    private final Map<String, Mapper<JsonNode, WeatherResponseModel>> mapperOfResponses;
    private final Map<String, Mapper<JsonNode, List<WeatherResponseModel>>> mapperOfListResponses;

    public MapperFactory() {
        mapperOfResponses = initMapper();
        mapperOfListResponses = initListMapper();
    }

    public Map<String, Mapper<JsonNode, WeatherResponseModel>> getMapperOfResponses() {
        return mapperOfResponses;
    }

    public Map<String, Mapper<JsonNode, List<WeatherResponseModel>>> getMapperOfListResponses() {
        return mapperOfListResponses;
    }

    private Map<String, Mapper<JsonNode, WeatherResponseModel>> initMapper() {
        final Map<String, Mapper<JsonNode, WeatherResponseModel>> mapperOfResponses = new HashMap<>();


        mapperOfResponses.put(
                weatherGisApiSource,
                jsonNode -> {
                    WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
                    weatherResponseModel.setDay(jsonNode.get("location").get("localtime").asText());
                    weatherResponseModel.setCityName(jsonNode.get("location").get("name").asText());
                    weatherResponseModel.setTemperature(jsonNode.get("current").get("temp_c").asDouble());
                    weatherResponseModel.setSpeedOfWind(jsonNode.get("current").get("wind_kph").asDouble());
                    weatherResponseModel.setWeatherCharacteristic(jsonNode.get("current").get("condition").get("text").asText());
                    weatherResponseModel.setWeatherApiSource(weatherGisApiSource);
                    return weatherResponseModel;
                }
        );
//        final String weatherYandexApiSource = YndexService.class.getSimpleName().toLowerCase(Locale.ROOT);
//        mapperOfResponses.put(
//                weatherYandexApiSource,
//                jsonNode -> {
//                    WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
//                    weatherResponseModel.setCityName(jsonNode.get("location").get("name").asText());
//                    weatherResponseModel.setTemperature(jsonNode.get("current").get("temp_c").asDouble());
//                    weatherResponseModel.setSpeedOfWind(jsonNode.get("current").get("wind_kph").asDouble());
//                    weatherResponseModel.setWeatherCharacteristic(jsonNode.get("current").get("condition").get("text").asText());
//                    weatherResponseModel.setWeatherApiSource(weatherGisApiSource);
//                    return weatherResponseModel;
//                }
//        );
        return mapperOfResponses;
    }

    private Map<String, Mapper<JsonNode, List<WeatherResponseModel>>> initListMapper() {
        final Map<String, Mapper<JsonNode, List<WeatherResponseModel>>> mapperOfListResponses = new HashMap<>();

        mapperOfListResponses.put(
                weatherGisApiSource,
                jsonNode -> {
                    List<WeatherResponseModel> weatherResponseModels = new ArrayList<>();
                    String cityName = jsonNode.get("location").get("name").asText();

                    ArrayNode weekResponseArrayJsonNode = (ArrayNode) jsonNode.get("forecast").get("forecastday");
                    weekResponseArrayJsonNode.forEach(forecastDayNode -> {
                        WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
                        weatherResponseModel.setCityName(cityName);
                        weatherResponseModel.setWeatherApiSource(weatherGisApiSource);
                        weatherResponseModel.setDay(forecastDayNode.get("date").asText());
                        weatherResponseModel.setTemperature(forecastDayNode.get("day").get("avgtemp_c").asDouble());
                        weatherResponseModel.setSpeedOfWind(forecastDayNode.get("day").get("maxwind_kph").asDouble());
                        weatherResponseModel.setWeatherCharacteristic(forecastDayNode.get("day").get("condition").get("text").asText());
                        weatherResponseModels.add(weatherResponseModel);
                    });
                    return weatherResponseModels;
                }
        );
//        final String weatherYandexApiSource = YndexService.class.getSimpleName().toLowerCase(Locale.ROOT);
//        mapperOfResponses.put(
//                weatherYandexApiSource,
//                jsonNode -> {
//                    WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
//                    weatherResponseModel.setCityName(jsonNode.get("location").get("name").asText());
//                    weatherResponseModel.setTemperature(jsonNode.get("current").get("temp_c").asDouble());
//                    weatherResponseModel.setSpeedOfWind(jsonNode.get("current").get("wind_kph").asDouble());
//                    weatherResponseModel.setWeatherCharacteristic(jsonNode.get("current").get("condition").get("text").asText());
//                    weatherResponseModel.setWeatherApiSource(weatherGisApiSource);
//                    return weatherResponseModel;
//                }
//        );
        return mapperOfListResponses;
    }
}
