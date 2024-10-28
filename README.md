# GeoNetwork opensource services

GeoNetwork microservices is GeoNetwork ready to use in the cloud through dockerized microservices. It will work along with GeoNetwork 4 as it will only provide part of the application functionalities.

* [Configuration](modules/support-services/configuring/README.md)
* [Authorizing by using JWT](modules/services/authorizing/README.md)
* [Searching](modules/services/authorizing/README.md)
* [Routing](modules/services/routing/README.md)
* [Indexing](modules/services/indexing/README.md)
* [OGC API Records](modules/services/ogc-api-records/README.md)

## Services architecture

![Overall architecture](doc/img/gnmicroservices.svg)

## Build & run

### Requirements
Those components are created with the following requirements:
* Java 11 JDK
* Maven 3.6.3
* Docker (optional)
* [core-geonetwork:4.2.11](https://github.com/geonetwork/core-geonetwork/releases/tag/4.2.11), might need a local build, it's not available on any published maven repository?

### Building
To build the services:
> **_NOTE:_**  It will build all the services.
> - authorizing
> - gateway
> - indexing
> - ogc-api-records
> - searching

```shell script
./mvnw clean install
```

For a quicker build, you can skip `checkstyle`, tests and docker image build with:

```shell script
./mvnw clean install -Drelax -P-docker
```

### Running with docker
The simple build command above created the docker images.

Now run the docker composition as follows, the first time it might need to download some additional images for the rabbitmq event broker and the postgresql config database:

> **_NOTE:_**  This will run the whole docker composition:
> the microservice architecture + all services.

```shell script
docker-compose up -d
```

If some services fail to start and report config server error:
```
gateway_1        | java.lang.IllegalStateException: No instances found of configserver (config-service)
```

Restart `docker-compose up -d` to launch again the missing services. The config server being up, they will start properly.


Once services are up and running, access GeoNetwork from http://localhost:9900/geonetwork.

Run `docker-compose logs -f` to watch startup progress of all services.

### Calling services

Test the service using the token:

```shell script
# Authenticate
USERNAME=admin
PASSWORD=admin

gn_token=$( \
    curl test-client:noonewilleverguess@127.0.0.1:9900/oauth/token \
         -dgrant_type=password -dscope=any \
         -dusername=$USERNAME -dpassword=$PASSWORD \
        | jq -r '.access_token')

# Testing the token
gn_auth_header=$(echo "Authorization: Bearer $gn_token")
curl 127.0.0.1:9900/secured -H "$gn_auth_header"


# Search service (TODO: Need to be accessible to anonymous)
curl 127.0.0.1:9900/search \
    -H "Accept: application/json" \
    -H "Content-type: application/json" \
    -H "$gn_auth_header" \
    -X POST \
    -d '{"from": 0, "size": 0, "query": {"query_string": {"query": "+isTemplate:n"}}}' | jq -r '.hits.total.value'
```

### Development/debug

Developments are made on https://github.com/geonetwork/geonetwork-microservices

To run one service directly without docker, use the `standalone` profile.

```shell script
mvn spring-boot:run -Dspring-boot.run.profiles=dev,standalone -f modules/services/indexing/
```
You can also run the service from the service root folder also eg `modules/services/ogc-api-records` and refer to the [documentation](https://github.com/geonetwork/geonetwork-microservices/tree/main/modules/services/ogc-api-records#run-the-service-as-a-standalone-spring-boot-app) of the service 

To run all services independently, start the event bus rabbitmq + support services, then start apps in order:
```shell script
docker-compose up -d rabbitmq discovery config

mvn spring-boot:run -Dspring-boot.run.profiles=dev,standalone -f modules/support-services/discovery
mvn spring-boot:run -Dspring-boot.run.profiles=dev,standalone -f modules/support-services/configuring
mvn spring-boot:run -Dspring-boot.run.profiles=dev,standalone -f modules/services/indexing
mvn spring-boot:run -Dspring-boot.run.profiles=dev,standalone -f modules/services/ogc-api-records
...
```

To work on a microservice, start the docker containers and then run the service separetely:
```shell script
SERVER_PORT=9901 mvn spring-boot:run -Dspring-boot.run.profiles=dev,standalone -f modules/services/searching
```


## Bugs

## Roadmap

TODO

## Contributing

To set license header use:

```shell script
./mvnw license:format
```


## Status

This is a feasibility exploration of moving from GeoNetwork monolithic application to a more scalable architecture. The main ideas are:
* improve discoverability & search availability by creating a dedicated search service that can be replicated
* better Search Engine Optimization by implementing an OGC API records service with a landing page builder mechanism
* be in capacity to have background tasks like harvester and indexing that do not alter performances of the main web application.

