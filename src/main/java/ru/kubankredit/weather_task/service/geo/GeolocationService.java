package ru.kubankredit.weather_task.service.geo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kubankredit.weather_task.exception.WeatherServiceException;
import ru.kubankredit.weather_task.model.PointPos;
import ru.kubankredit.weather_task.service.AuthTokenProperties;
import ru.kubankredit.weather_task.service.Services;

@Component
public class GeolocationService implements GeoService<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationService.class);

    private static final String URL = "https://geocode-maps.yandex.ru/1.x/";
    private static final String CITY_PREFIX = "город ";

    private final RestTemplate restTemplate;
    private final String token;


    public GeolocationService(RestTemplate restTemplate, AuthTokenProperties authTokenProperties) {
        this.restTemplate = restTemplate;
        token = authTokenProperties.getYandexGeo().getToken();
        LOGGER.info("Service Geolocation is starting");
    }

    @Override
    public PointPos getPoint(String cityName) {
        String fullCityName = CITY_PREFIX + cityName;
        String uriForApiGis = UriComponentsBuilder.fromUriString(URL)
                .queryParam("apikey", token)
                .queryParam("geocode", fullCityName)
                .queryParam("format", "json")
                .queryParam("result", "1")
                .queryParam("kind", "locality")
                .build().toUriString();
        LOGGER.debug("Create URI is {}", uriForApiGis);
        PointPos pointPos = new PointPos();
        try {
            ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(uriForApiGis, JsonNode.class);
            if(responseEntity.hasBody()) {
                LOGGER.debug("responseEntity: {}", responseEntity);
                JsonNode jsonBodyOfResponseEntity = responseEntity.getBody();
                if (jsonBodyOfResponseEntity!= null && !jsonBodyOfResponseEntity.isNull()) {
                    ArrayNode jsonNode = (ArrayNode) jsonBodyOfResponseEntity.get("response").get("GeoObjectCollection").get("featureMember");
                    jsonNode.forEach(jsonNode1 -> {
                        String point = jsonNode1.get("GeoObject").get("Point").get("pos").asText();
                        String[] pointArray = point.split(" ");
                        pointPos.setLatitude(pointArray[1]);
                        pointPos.setLongitude(pointArray[0]);
                    });
                }
            }
        } catch (RuntimeException exception) {
            throw new WeatherServiceException(exception.getMessage(), Services.GEO.getName());
        }

        LOGGER.info("Геолокация города {} получена = {}", cityName, pointPos);
        return pointPos;
    }
}
