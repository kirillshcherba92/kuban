package ru.kubankredit.weather_task.service;

public enum Services {
    YANDEX("YANDEX"),
    GIS("GIS"),
    GEO("GEO"),
    ALL("ALL");

    private String name;

    Services(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
