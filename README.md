# vdnh-back

Модуль vdnh-back предназначен для получения из базы данных и передачи в модуль vdnh-front всей информации, необходимой для отображения интерактивной карты и построения маршрутов, по протоколу HTTP (или HTTPS) в формате JSON.

Адрес Swagger UI приложения, развёрнутого в Cloud:

https://let-squad.ml/swagger-ui

### Инструкция по сборке и запуску 

#### Сборка приложения

Команда для сборки:

`mvn clean install`

В случае успешной сборки приложения в рабочей директории будет создана поддиректория target с приложением, а в лог будет выведено сообщение:

`BUILD SUCCESS`

#### Запуск приложения

Для настройки подключения к базе данных и к Mapbox в рабочей директории необходимо создать файл application.yml со следующим содержимым:

```yaml
spring:
  datasource:
    url: 'jdbc:postgresql://<DATABASE_HOST>/<DATABASE_NAME>'
    username: <DATABASE_USERNAME>
    password: <DATABASE_PASSWORD>
mapbox:
  access-token: <MAPBOX_KEY>
```

Команда для запуска (из директории target):

`java -jar vdnh-backend.jar`

При первом запуске приложения автоматически будет создана структура таблиц в базе данных при помощи Liquibase

После успешной инициализации базы данных в лог будет выведено сообщение:

`ChangeSet db/changelog/sql/1_init_db.sql::raw::includeAll ran successfully`

После успешного подключения к базе данных и запуска приложения в лог будут выведены сообщения вида:

```
Tomcat started on port(s): 8080 (http) with context path ''
Started VdnhApplicationKt in 6.946 seconds (JVM running for 7.586)
```

По умолчанию приложение запускается на 8080 порту, при необходимости переопределить порт нужно в файл application.yml добавить значение:

```yaml
server:
  port: <SERVER_PORT>
```
