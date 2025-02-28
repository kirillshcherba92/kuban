package ru.kubankredit.weather_task.service.yandex;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.kubankredit.weather_task.model.WeatherResponseModel;
import ru.kubankredit.weather_task.service.AuthTokenProperties;
import ru.kubankredit.weather_task.service.WeatherService;

import java.util.List;

@Component
@Conditional(ConditionYandexService.class)
@EnableConfigurationProperties(value = AuthTokenProperties.class)
public class YndexService implements WeatherService {

    private final static String BASE_URL = "";
    private final String token;


    private final RestTemplate restTemplate;
    private final AuthTokenProperties authTokenProperties;

    public YndexService(RestTemplate restTemplate, AuthTokenProperties authTokenProperties) {
        this.restTemplate = restTemplate;
        this.authTokenProperties = authTokenProperties;
        this.token = this.authTokenProperties.getYandex().getToken();
    }

    @Override
    public WeatherResponseModel getCurrentWeather(String cityName) {
        System.out.println("YndexService");
        return null;
    }

    @Override
    public List<WeatherResponseModel> getWeekWeather(String cityName) {
        System.out.println("YndexService week");
        return null;
    }
}
