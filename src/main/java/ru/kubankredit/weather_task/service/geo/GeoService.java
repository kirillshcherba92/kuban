package ru.kubankredit.weather_task.service.geo;

import ru.kubankredit.weather_task.model.PointPos;

public interface GeoService<T> {
    PointPos getPoint(T t);
}
