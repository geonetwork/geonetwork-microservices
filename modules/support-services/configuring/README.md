# Configuration service

Start the service using:
```
mvn spring-boot:run
```

It uses a file storage for storing the configuration for all services.

Configuration authentication is required using user `gn` with password `gn`.

```yaml
spring:
  cloud:
    config:
      uri: http://localhost:9999
      name: geonetwork
      username: gn
      password: gn
```


Configuration can be retrieved using entry point like http://localhost:9999/geonetwork-dev.json.

Configuration can be refreshed on all microservices connected to the bus using:

```shell script
curl -X POST 127.0.0.1:9999/actuator/bus-refresh
```
