package ru.kubankredit.weather_task.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api-service")
public class AuthTokenProperties {
    private YandexServiceProperties yandex;
    private GisServiceProperties gis;

    public YandexServiceProperties getYandex() {
        return yandex;
    }

    public void setYandex(YandexServiceProperties yandex) {
        this.yandex = yandex;
    }

    public GisServiceProperties getGis() {
        return gis;
    }

    public void setGis(GisServiceProperties gis) {
        this.gis = gis;
    }

    public static class YandexServiceProperties {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class GisServiceProperties{
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}

