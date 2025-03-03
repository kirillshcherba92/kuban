package ru.kubankredit.weather_task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kubankredit.weather_task.model.WeatherResponseModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

@Component
public class CacheEvictScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheEvictScheduler.class);

    private final DateTimeFormatter dateTimeFormatterGis = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DateTimeFormatter dateTimeFormatterYandex = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final CacheManager cacheManager;

    public CacheEvictScheduler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60) // ккаждый час проверка
    public void evictCurrentWeatherGisCache() {
        Cache currentWeather = cacheManager.getCache("currentWeatherGis");
        extracted(currentWeather, dateTimeFormatterGis);
        LOGGER.info("Очистка кеша для хранилища \"currentWeatherGis\".");

    }

    @Scheduled(fixedRate = 1000 * 60 * 60) // каждый час проверка
    public void evictCurrentWeatherYandexCache() {
        Cache currentWeather = cacheManager.getCache("currentWeatherYandex");
        extracted(currentWeather, dateTimeFormatterYandex);
        LOGGER.info("Очистка кеша для хранилища \"currentWeatherYandex\".");
    }

    private void extracted(Cache currentWeather, DateTimeFormatter dateTimeFormatter) {
        if (currentWeather != null) {
            Map<String, WeatherResponseModel> currentWeatherMap = (Map<String, WeatherResponseModel>) currentWeather.getNativeCache();
            Set<String> currentWeatherCacheKeys = currentWeatherMap.keySet();
            currentWeatherCacheKeys.forEach(key -> {
                WeatherResponseModel weatherResponseModel = currentWeatherMap.get(key);
                LocalDate date = LocalDate.parse(weatherResponseModel.getDay(), dateTimeFormatter);
                if (date.isBefore(LocalDate.now())) {
                    currentWeatherMap.remove(key);
                }
            });
        }
    }
}
