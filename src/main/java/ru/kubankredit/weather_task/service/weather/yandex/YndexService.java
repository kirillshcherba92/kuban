package ru.kubankredit.weather_task.service.weather.yandex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kubankredit.weather_task.exception.WeatherServiceException;
import ru.kubankredit.weather_task.model.PointPos;
import ru.kubankredit.weather_task.model.WeatherResponseModel;
import ru.kubankredit.weather_task.service.AuthTokenProperties;
import ru.kubankredit.weather_task.service.MapperFactory;
import ru.kubankredit.weather_task.service.Services;
import ru.kubankredit.weather_task.service.WeatherService;
import ru.kubankredit.weather_task.service.geo.GeoService;

import java.net.URI;
import java.util.List;

import static ru.kubankredit.weather_task.exception.ContainerOfAnswer.MAP_OF_EXCEPTION_ANSWER_SERVICE_YANDEX;

@Component
@Conditional(ConditionYandexService.class)
@EnableConfigurationProperties(value = AuthTokenProperties.class)
public class YndexService implements WeatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(YndexService.class);

    private static final String BASE_URL = "http://api.weather.yandex.ru/v2/forecast";
    private static final String HEADER_TOKEN_KEY = "X-Yandex-Weather-Key";


    private final String token;
    private final String serviceName;
    private final HttpHeaders httpHeaders = new HttpHeaders();


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final MapperFactory mapperFactory;
    private final GeoService<String> geolocationService;

    public YndexService(RestTemplate restTemplate, AuthTokenProperties authTokenProperties, ObjectMapper objectMapper, MapperFactory mapperFactory, GeoService<String> geolocationService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.geolocationService = geolocationService;
        this.token = authTokenProperties.getYandex().getToken();
        this.mapperFactory = mapperFactory;
        serviceName = Services.YANDEX.getName();
        httpHeaders.add(HEADER_TOKEN_KEY, token);
        LOGGER.info("Service {} is starting", serviceName);
    }

    @Override
    @Cacheable(value = "currentWeatherYandex", key = "#cityName", unless = "#result == null ")
    public WeatherResponseModel getCurrentWeather(String cityName) {
        PointPos point = geolocationService.getPoint(cityName);

        String uriForApiGis = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("lat", point.getLatitude())
                .queryParam("lon", point.getLongitude())
                .queryParam("extra", false)
                .queryParam("hours", false)
                .queryParam("limit", 1)
                .build().toUriString();
        LOGGER.debug("Create URI is {}", uriForApiGis);
        JsonNode jsonBodyOfResponseEntity = sendRequestToApiService(uriForApiGis);
        WeatherResponseModel weatherResponseModelGisService = mapperFactory.getMapperOfResponses()
                .get(serviceName)
                .mapFrom(jsonBodyOfResponseEntity);
        weatherResponseModelGisService.setCityName(cityName);
        return weatherResponseModelGisService;
    }

    @Override
    public List<WeatherResponseModel> getWeekWeather(String cityName) {
        PointPos point = geolocationService.getPoint(cityName);

        String uriForApiGis = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("lat", point.getLatitude())
                .queryParam("lon", point.getLongitude())
                .queryParam("extra", false)
                .queryParam("hours", false)
                .queryParam("limit", 7)
                .build().toUriString();
        LOGGER.debug("Create URI is {}", uriForApiGis);
        JsonNode jsonBodyOfResponseEntity = sendRequestToApiService(uriForApiGis);

        return mapperFactory.getMapperOfListResponses()
                .get(serviceName)
                .mapFrom(jsonBodyOfResponseEntity)
                .stream()
                .peek(weatherResponseModel -> weatherResponseModel.setCityName(cityName))
                .toList();
    }

    @Override
    public String getName() {
        return serviceName;
    }

    private JsonNode sendRequestToApiService(String uriForApiGis) {
        RequestEntity<JsonNode> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, URI.create(uriForApiGis));
        ResponseEntity<JsonNode> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);
            LOGGER.debug("responseEntity: {}", responseEntity);
        } catch (final HttpClientErrorException restClientException) {
            final String responseBodyAsString = restClientException.getResponseBodyAsString();
            String message = responseBodyAsString;
            JsonNode bodyOfRestClientException = null;
            try {
                bodyOfRestClientException = objectMapper.readTree(responseBodyAsString);
            } catch (JsonProcessingException exception) {
                throw new WeatherServiceException(exception.getMessage(), serviceName);
            }
            JsonNode expMessageJson = bodyOfRestClientException.get("message");
            if (expMessageJson != null) {
                message = MAP_OF_EXCEPTION_ANSWER_SERVICE_YANDEX.get(expMessageJson.asText());
            }
            LOGGER.error(message);
            throw new WeatherServiceException(message, serviceName);
        }
        return responseEntity.getBody();
    }
}
