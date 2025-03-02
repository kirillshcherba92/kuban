package ru.kubankredit.weather_task.service.weather.yandex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.stream.Collectors;

import static ru.kubankredit.weather_task.exception.ContainerOfAnswer.MAP_OF_EXCEPTION_ANSWER_SERVICE_YANDEX;

@Component
@Conditional(ConditionYandexService.class)
@EnableConfigurationProperties(value = AuthTokenProperties.class)
public class YndexService implements WeatherService {

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

        JsonNode jsonBodyOfResponseEntity = sendRequestToApiService(uriForApiGis);

        return mapperFactory.getMapperOfListResponses()
                .get(serviceName)
                .mapFrom(jsonBodyOfResponseEntity)
                .stream()
                .peek(weatherResponseModel -> weatherResponseModel.setCityName(cityName))
                .collect(Collectors.toList());
    }

    private JsonNode sendRequestToApiService(String uriForApiGis) {
        RequestEntity requestEntity = new RequestEntity(httpHeaders, HttpMethod.GET, URI.create(uriForApiGis));
        ResponseEntity<JsonNode> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);
        } catch (HttpClientErrorException restClientException) {
            JsonNode bodyOfRestClientException = null;
            try {
                bodyOfRestClientException = objectMapper.readTree(restClientException.getResponseBodyAsString());
            } catch (JsonProcessingException exception) {
                throw new WeatherServiceException(exception.getMessage(), serviceName);
            }
            String message = MAP_OF_EXCEPTION_ANSWER_SERVICE_YANDEX.get(bodyOfRestClientException.get("message").asText());
            throw new WeatherServiceException(message, serviceName);
        }
        return responseEntity.getBody();
    }
}
