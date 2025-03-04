###### Общие:
Использовались сервисы погоды YANDEX и weather.api.com.
Для использования сервисов необходимо получить токены для их публичных API.
Полученные токены необходимо добавить в [application-dev.yml](src/main/resources/application-dev.yml),
под соответствующими названиями.

###### Инструкция по запуску:
Локально проект запускает через настройку Environment variable
(--spring.config.location=classpath:./application.properties, file:./src/main/resources/application-dev.yml),
так есть разделение настроечных файлов. Концепция настроечных файлов хороша тем,
что они более гибчи, чем профилирование. И так же может гибко подставляться в docker-compose.

###### Примеры:
Для тестирования подготовлен список тестовых запросов [testREq.http](src/main/resources/testREq.http).

###### Планы
Кеширование +
Обработка REST ошибок +
Логирование +
Докер +
Дока
Тесты (JUNIT)
