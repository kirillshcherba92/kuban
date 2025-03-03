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

    private static final String LOCATION =  "location";
    private static final String CURRENT =  "current";
    private static final String CONDITION =  "condition";
    private static final String PART =  "parts";
    private static final String FACT =  "fact";
    private static final String DAY =  "day";
    private static final String FORECASTS =  "forecasts";

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
        final Map<String, Mapper<JsonNode, WeatherResponseModel>> includesMapperOfResponses = new HashMap<>();

        includesMapperOfResponses.put(
                weatherGisApiSource,
                jsonNode -> {
                    WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
                    weatherResponseModel.setDay(jsonNode.get(LOCATION).get("localtime").asText());
                    weatherResponseModel.setCityName(jsonNode.get(LOCATION).get("name").asText());
                    weatherResponseModel.setTemperature(jsonNode.get(CURRENT).get("temp_c").asDouble());
                    weatherResponseModel.setSpeedOfWind(
                            BigDecimal.valueOf(
                                    ((jsonNode.get(CURRENT).get("wind_kph").asDouble() * 1000) / 3600)
                            ).setScale(2, BigDecimal.ROUND_UP)
                                    .doubleValue()
                    );
                    weatherResponseModel.setWeatherCharacteristic(jsonNode.get(CURRENT).get(CONDITION).get("text").asText());
                    weatherResponseModel.setWeatherApiSource(weatherGisApiSource);
                    return weatherResponseModel;
                }
        );
        includesMapperOfResponses.put(
                weatherYandexApiSource,
                jsonNode -> {
                    WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
                    weatherResponseModel.setDay(((ArrayNode) jsonNode.get(FORECASTS)).get(0).get("date").asText());
                    weatherResponseModel.setTemperature(jsonNode.get(FACT).get("temp").asDouble());
                    weatherResponseModel.setSpeedOfWind(jsonNode.get(FACT).get("wind_speed").asDouble());
                    weatherResponseModel.setWeatherCharacteristic(jsonNode.get(FACT).get(CONDITION).asText());
                    weatherResponseModel.setWeatherApiSource(weatherYandexApiSource);
                    return weatherResponseModel;
                }
        );
        return includesMapperOfResponses;
    }

    private Map<String, Mapper<JsonNode, List<WeatherResponseModel>>> initListMapper() {
        final Map<String, Mapper<JsonNode, List<WeatherResponseModel>>> includesMapperOfListResponses = new HashMap<>();

        includesMapperOfListResponses.put(
                weatherGisApiSource,
                jsonNode -> {
                    List<WeatherResponseModel> weatherResponseModels = new ArrayList<>();
                    String cityName = jsonNode.get(LOCATION).get("name").asText();

                    ArrayNode weekResponseArrayJsonNode = (ArrayNode) jsonNode.get("forecast").get("forecastday");
                    weekResponseArrayJsonNode.forEach(forecastDayNode -> {
                        WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
                        weatherResponseModel.setCityName(cityName);
                        weatherResponseModel.setWeatherApiSource(weatherGisApiSource);
                        weatherResponseModel.setDay(forecastDayNode.get("date").asText());
                        weatherResponseModel.setTemperature(forecastDayNode.get(DAY).get("avgtemp_c").asDouble());
                        weatherResponseModel.setSpeedOfWind(forecastDayNode.get(DAY).get("maxwind_kph").asDouble());
                        weatherResponseModel.setWeatherCharacteristic(forecastDayNode.get(DAY).get(CONDITION).get("text").asText());
                        weatherResponseModels.add(weatherResponseModel);
                    });
                    return weatherResponseModels;
                }
        );
        includesMapperOfListResponses.put(
                weatherYandexApiSource,
                jsonNode -> {
                    List<WeatherResponseModel> weatherResponseModels = new ArrayList<>();

                    ArrayNode weekResponseArrayJsonNode = (ArrayNode) jsonNode.get(FORECASTS);
                    weekResponseArrayJsonNode.forEach(forecastDayNode -> {
                        WeatherResponseModel weatherResponseModel = new WeatherResponseModel();
                        weatherResponseModel.setWeatherApiSource(weatherYandexApiSource);
                        weatherResponseModel.setDay(forecastDayNode.get("date").asText());
                        weatherResponseModel.setTemperature(forecastDayNode.get(PART).get(DAY).get("temp_avg").asDouble());
                        weatherResponseModel.setSpeedOfWind(forecastDayNode.get(PART).get(DAY).get("wind_speed").asDouble());
                        weatherResponseModel.setWeatherCharacteristic(forecastDayNode.get(PART).get(DAY).get(CONDITION).asText());
                        weatherResponseModels.add(weatherResponseModel);
                    });
                    return weatherResponseModels;
                }
        );
        return includesMapperOfListResponses;
    }
}
