package ru.kubankredit.weather_task.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherResponseModel {
    @JsonProperty(value = "Дата")
    private String day;
    @JsonProperty(value = "город")
    private String cityName;
    @JsonProperty(value = "температура(С)")
    private Double temperature;
    @JsonProperty(value = "скорость ветра м/с")
    private Double speedOfWind;
    @JsonProperty(value = "характеристика погоды")
    private String weatherCharacteristic;
    @JsonProperty(value = "Сервис API")
    private String weatherApiSource;

    public WeatherResponseModel(String day, String cityName, Double temperature, Double speedOfWind, String weatherCharacteristic, String weatherApiSource) {
        this.day = day;
        this.cityName = cityName;
        this.temperature = temperature;
        this.speedOfWind = speedOfWind;
        this.weatherCharacteristic = weatherCharacteristic;
        this.weatherApiSource = weatherApiSource;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public WeatherResponseModel() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getSpeedOfWind() {
        return speedOfWind;
    }

    public void setSpeedOfWind(Double speedOfWind) {
        this.speedOfWind = speedOfWind;
    }

    public String getWeatherCharacteristic() {
        return weatherCharacteristic;
    }

    public void setWeatherCharacteristic(String weatherCharacteristic) {
        this.weatherCharacteristic = weatherCharacteristic;
    }

    public String getWeatherApiSource() {
        return weatherApiSource;
    }

    public void setWeatherApiSource(String weatherApiSource) {
        this.weatherApiSource = weatherApiSource;
    }

    @Override
    public String toString() {
        return "WeatherResponseModel{" +
                "cityName='" + cityName + '\'' +
                ", temperature=" + temperature +
                ", speedOfWind=" + speedOfWind +
                ", weatherCharacteristic='" + weatherCharacteristic + '\'' +
                ", weatherApiSource='" + weatherApiSource + '\'' +
                '}';
    }
}
