package ru.kubankredit.weather_task.service.weather.gis;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kubankredit.weather_task.exception.WeatherServiceException;
import ru.kubankredit.weather_task.model.WeatherResponseModel;
import ru.kubankredit.weather_task.service.AuthTokenProperties;
import ru.kubankredit.weather_task.service.MapperFactory;
import ru.kubankredit.weather_task.service.Services;
import ru.kubankredit.weather_task.service.WeatherService;

import java.util.List;

import static ru.kubankredit.weather_task.exception.ContainerOfAnswer.MAP_OF_EXCEPTION_ANSWER_SERVICE_GIS;

@Component
@Conditional(ConditionGisService.class)
@EnableConfigurationProperties(value = AuthTokenProperties.class)
public class GisService implements WeatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GisService.class);

    private static final String BASE_URL = "http://api.weatherapi.com/v1/";
    private static final String CURRENT_URL_PATH = "current.json";
    private static final String WEEK_URL_PATH = "forecast.json";

    private final String apiToken;
    private final String serviceName;

    private final RestTemplate restTemplate;
    private final MapperFactory mapperFactory;


    public GisService(RestTemplate restTemplate, final AuthTokenProperties authTokenProperties, MapperFactory mapperFactory) {
        this.restTemplate = restTemplate;
        apiToken = authTokenProperties.getGis().getToken();
        this.mapperFactory = mapperFactory;
        serviceName = Services.GIS.getName();
        LOGGER.info("Service {} is starting", serviceName);
    }

    @Override
    @Cacheable(value = "currentWeatherGis", key = "#cityName", unless = "#result == null ")
    public WeatherResponseModel getCurrentWeather(String cityName) {
        String uriForApiGis = UriComponentsBuilder.fromUriString(BASE_URL)
                .path(CURRENT_URL_PATH)
                .queryParam("key", apiToken)
                .queryParam("q", cityName)
                .queryParam("aqi", "no")
                .build().toUriString();
        LOGGER.debug("Create URI is {}", uriForApiGis);
        ResponseEntity<JsonNode> responseEntity = sendRequestToApiService(uriForApiGis);
        LOGGER.debug("responseEntity: {}", responseEntity);
        JsonNode jsonBodyOfResponseEntity = responseEntity.getBody();
        return mapperFactory.getMapperOfResponses()
                .get(serviceName)
                .mapFrom(jsonBodyOfResponseEntity);
    }

    @Override
    public List<WeatherResponseModel> getWeekWeather(String cityName) {
        String uriForApiGis = UriComponentsBuilder.fromUriString(BASE_URL)
                .path(WEEK_URL_PATH)
                .queryParam("key", apiToken)
                .queryParam("q", cityName)
                .queryParam("days", 7)
                .queryParam("aqi", "no")
                .queryParam("alerts", "no")
                .build().toUriString();
        LOGGER.debug("Create URI is {}", uriForApiGis);
        ResponseEntity<JsonNode> responseEntity = sendRequestToApiService(uriForApiGis);
        LOGGER.debug("responseEntity: {}", responseEntity);
        JsonNode jsonBodyOfResponseEntity = responseEntity.getBody();
        return mapperFactory.getMapperOfListResponses()
                .get(serviceName)
                .mapFrom(jsonBodyOfResponseEntity);
    }

    private ResponseEntity<JsonNode> sendRequestToApiService(String uriForApiGis) {
        ResponseEntity<JsonNode> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(uriForApiGis, JsonNode.class);
        } catch (final HttpClientErrorException restClientException) {
            String message = restClientException.getResponseBodyAsString();
            final JsonNode bodyOfRestClientException = restClientException.getResponseBodyAs(JsonNode.class);
            try {
                message = MAP_OF_EXCEPTION_ANSWER_SERVICE_GIS.get(bodyOfRestClientException.get("error").get("code").asInt());
            } catch (final RuntimeException exception) {
                message = exception.getMessage();
            }
            LOGGER.error(message);
            throw new WeatherServiceException(message, serviceName);
        }
        return responseEntity;
    }
}
