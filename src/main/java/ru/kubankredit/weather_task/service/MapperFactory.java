package ru.kubankredit.weather_task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;
import ru.kubankredit.weather_task.model.WeatherResponseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapperFactory {
    private final String weatherGisApiSource = Services.GIS.getName();
    private final String weatherYandexApiSource = Services.YANDEX.getName();

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
                    weatherResponseModel.setSpeedOfWind(
                            BigDecimal.valueOf(
                                    ((jsonNode.get("current").get("wind_kph").asDouble() * 1000) / 3600)
                            ).setScale(2, BigDecimal.ROUND_UP)
                                    .doubleValue()
                    );
                    weatherResponseModel.setWeatherCharacteristic(jsonNode.get("current").get("condition").get("text").asText());
                    weatherResponseModel.setWeatherApiSource(weatherGisApiSource);
                    return weatherResponseModel;
                }
        );
        mapperOfResponses.put(
                weatherYandexApiSource,
                jsonNode -> {
                    WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
                    weatherResponseModel.setDay(((ArrayNode) jsonNode.get("forecasts")).get(0).get("date").asText());
                    weatherResponseModel.setTemperature(jsonNode.get("fact").get("temp").asDouble());
                    weatherResponseModel.setSpeedOfWind(jsonNode.get("fact").get("wind_speed").asDouble());
                    weatherResponseModel.setWeatherCharacteristic(jsonNode.get("fact").get("condition").asText());
                    weatherResponseModel.setWeatherApiSource(weatherYandexApiSource);
                    return weatherResponseModel;
                }
        );
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
        mapperOfListResponses.put(
                weatherYandexApiSource,
                jsonNode -> {
                    List<WeatherResponseModel> weatherResponseModels = new ArrayList<>();

                    ArrayNode weekResponseArrayJsonNode = (ArrayNode) jsonNode.get("forecasts");
                    weekResponseArrayJsonNode.forEach(forecastDayNode -> {
                        WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
                        weatherResponseModel.setWeatherApiSource(weatherYandexApiSource);
                        weatherResponseModel.setDay(forecastDayNode.get("date").asText());
                        weatherResponseModel.setTemperature(forecastDayNode.get("parts").get("day").get("temp_avg").asDouble());
                        weatherResponseModel.setSpeedOfWind(forecastDayNode.get("parts").get("day").get("wind_speed").asDouble());
                        weatherResponseModel.setWeatherCharacteristic(forecastDayNode.get("parts").get("day").get("condition").asText());
                        weatherResponseModels.add(weatherResponseModel);
                    });
                    return weatherResponseModels;
                }
        );
        return mapperOfListResponses;
    }
}
