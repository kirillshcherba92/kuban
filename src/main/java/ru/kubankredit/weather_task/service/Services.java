package ru.kubankredit.weather_task.service;

public enum Services {
    YANDEX("YANDEX"),
    GIS("GIS");

    private String name;

    Services(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
