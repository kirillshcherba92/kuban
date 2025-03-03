package ru.kubankredit.weather_task.service.geo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kubankredit.weather_task.model.PointPos;
import ru.kubankredit.weather_task.service.AuthTokenProperties;

@Component
public class GeolocationService implements GeoService<String> {

    private static final String URL = "https://geocode-maps.yandex.ru/1.x/";
    private static final String CITY_PREFIX = "город ";

    private final RestTemplate restTemplate;
    private final String token;


    public GeolocationService(RestTemplate restTemplate, AuthTokenProperties authTokenProperties) {
        this.restTemplate = restTemplate;
        token = authTokenProperties.getYandexGeo().getToken();
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
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(uriForApiGis, JsonNode.class);
        JsonNode jsonBodyOfResponseEntity = responseEntity.getBody();

        ArrayNode jsonNode = (ArrayNode) jsonBodyOfResponseEntity.get("response").get("GeoObjectCollection").get("featureMember");
        PointPos pointPos = new PointPos();
        jsonNode.forEach(jsonNode1 -> {
            String point = jsonNode1.get("GeoObject").get("Point").get("pos").asText();
            String[] pointArray = point.split(" ");
            pointPos.setLatitude(pointArray[1]);
            pointPos.setLongitude(pointArray[0]);
        });
        return pointPos;
    }
}
