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


## API

> In the following API example calls, use `http://localhost:9900/dataviz/index` as the base URL if running the
docker composition (as proxied by the gateway service), or `http://localhost:10000` if running locally 
(e.g. from `mvn spring-boot:run` or the IDE).

### Get a sample DataQuery

```
GET /sampleQuery
```

```
curl -i localhost:9900/dataviz/index/sampleQuery
HTTP/1.1 200 OK
user-agent: curl/7.68.0
forwarded: proto=http;host="localhost:9900";for="192.168.176.1:56684"
x-forwarded-for: 192.168.176.1
x-forwarded-proto: http
x-forwarded-prefix: /dataviz/index
x-forwarded-port: 9900
x-forwarded-host: localhost:9900
Content-Type: application/json
Content-Length: 212

{
  "uri" : "http://ows.example.com/wfs?request=GetCapabilities",
  "layerName" : "topp:states",
  "encoding" : "UTF-8",
  "auth" : {
    "type" : "basic",
    "userName" : "user",
    "password" : "secret"
  }
}
```

### Read a single layer from a dataset

```
curl -H "Content-Type: application/json" localhost:9900/dataviz/index/read -d '{
  "uri" : "file:/tmp/datadir/data/shapefiles/states.shp",
  "layerName" : "states",
  "encoding" : "UTF-8"
}'
```