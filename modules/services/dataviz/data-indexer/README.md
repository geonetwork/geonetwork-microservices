# Data Indexing service

Service that consumes geospatial data streams and pushes each feature/record to an indexing service.

This is not a metadata harvester, but a data indexing service aiming at providing
an API to data visualization client applications in the form of streaming
data-points.


## Build

```
mvn clean install -Dcheckstyle.skip
```

> Using `-Dcheckstyle.skip` until figuring out how to apply a code formatter that'll make checkstyle happy

## Running

### Docker-compose

Using the `docker-compose.yml` composition at the project's root folder:

Either just run the whole composition:

```
docker-compose up -d data-indexer gateway
```

Or this app's required services:

```
docker-compose up -d data-indexer gateway
```

Note the `gateway` service is not required, but useful to test the API through the reverse proxy instead of hitting the internal service URL's directly.

### Local/debug launch

Make sure the services it depends on are running. From the root project directory:

```
docker-compose up -d config rabbitmq elasticsearch
```

```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Or the equivalent application launch configuration from the IDE (for example, running the `org.fao.geonet.dataviz.indexing.DatavizIndexingApp`
as a regular Java application with the `-Dspring.profiles.active=local` JVM argument).


