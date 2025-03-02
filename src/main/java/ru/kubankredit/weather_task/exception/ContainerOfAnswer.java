package ru.kubankredit.weather_task.exception;

import java.util.Map;

public final class ContainerOfAnswer {

    private ContainerOfAnswer() {
        throw new IllegalArgumentException("Don't create UTILITIES CLASS");
    }

    public static final Map<Integer, String> MAP_OF_EXCEPTION_ANSWER_SERVICE_GIS = Map.of(
            1003, "Параметр \"q\" не указан.",
            1005, "Неверный URL-адрес запроса API.",
            1006, "Не найдено местоположение.",
            9000, "Текст Json, переданный в массовом запросе, неверен. Пожалуйста, убедитесь, что это правильный json в кодировке utf-8.",
            9001, "Текст в формате Json содержит слишком много мест для массового запроса. Пожалуйста, не превышайте 50 в одном запросе.",
            1002, "Ключ API не предоставлен.",
            2006, "Предоставленный API-ключ недействителен",
            2007, "API-ключ превысил месячную квоту вызовов.",
            2008, "API-ключ был отключен.",
            2009, "Ключ API не имеет доступа к ресурсу. Пожалуйста, ознакомьтесь со страницей ценообразования, чтобы узнать, что разрешено в вашем плане подписки на API."
    );
}
