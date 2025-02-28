package ru.kubankredit.weather_task.service.gis;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kubankredit.weather_task.model.WeatherResponseModel;
import ru.kubankredit.weather_task.service.AuthTokenProperties;
import ru.kubankredit.weather_task.service.MapperFactory;
import ru.kubankredit.weather_task.service.Services;
import ru.kubankredit.weather_task.service.WeatherService;

import java.util.List;
import java.util.Locale;

@Component
@Conditional(ConditionGisService.class)
@EnableConfigurationProperties(value = AuthTokenProperties.class)
public class GisService implements WeatherService {

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
    }

    @Override
    public WeatherResponseModel getCurrentWeather(String cityName) {
        String uriForApiGis = UriComponentsBuilder.fromUriString(BASE_URL)
                .path(CURRENT_URL_PATH)
                .queryParam("key", apiToken)
                .queryParam("q", cityName)
                .queryParam("aqi", "no")
                .build().toUriString();
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(uriForApiGis, JsonNode.class);
        JsonNode jsonBodyOfResponseEntity = responseEntity.getBody();
        WeatherResponseModel weatherResponseModelGisService = mapperFactory.getMapperOfResponses()
                .get(serviceName)
                .mapFrom(jsonBodyOfResponseEntity);
        return weatherResponseModelGisService;
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
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(uriForApiGis, JsonNode.class);
        JsonNode jsonBodyOfResponseEntity = responseEntity.getBody();

        List<WeatherResponseModel> weatherResponseModelList = mapperFactory.getMapperOfListResponses()
                .get(serviceName)
                .mapFrom(jsonBodyOfResponseEntity);

        return weatherResponseModelList;
    }
}
