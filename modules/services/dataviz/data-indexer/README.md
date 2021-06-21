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


## Usage

> In the following API example calls, use `http://localhost:9900/dataviz/index` as the base URL if running the
docker composition (as proxied by the gateway service), or `http://localhost:10000` if running locally 
(e.g. from `mvn spring-boot:run` or the IDE).

## Sample requests

Function composition: read a shapefile from an HTTP URL, and reproject it to WGS84

```
curl -i -H "Content-Type: text/plain" localhost:10000/readAll,toWgs84 -d  https://raw.githubusercontent.com/geoserver/geoserver/main/data/release/data/sf/roads.shp
HTTP/1.1 200 OK
user-agent: curl/7.68.0
Content-Type: application/json
Content-Length: 455095

[ {
  "type" : "Feature",
  "@id" : "1",
  "geometry" : {
    "type" : "MultiLineString",
    "@name" : "the_geom",
    "@srs" : "EPSG:4326",
    "coordinates" : [ [ [ -103.76318962743072, 44.37493141111295 ], [ -103.7632921483645, 44.37517396282682 ], [ -103.76405089795956, 44.37670748824779 ], [ -103.76379179094388, 44.377545125729156 ], [ -103.76276983127362, 44.37836461516183 ], [ -103.76197421493964, 44.37890071027333 ], [ -103.76193273622258, 44.37908700228332 ], [ -103.76202254127257, 44.37918667850246 ], [ -103.76206646568123, 44.37929141388107 ], [ -103.76222445702847, 44.3794191843382 ], [ -103.763226713461, 44.38013689675946 ], [ -103.76391017741982, 44.38118653776573 ], [ -103.76401290698996, 44.381418109267294 ], [ -103.76485223827957, 44.38228801602651 ], [ -103.76499558832916, 44.38281097185956 ], [ -103.76504627360814, 44.38296518526334 ], [ -103.76583595572829, 44.38405424487977 ], [ -103.76579753422489, 44.384498627125744 ], [ -103.7657526826934, 44.38448870752633 ] ] ]
  },
  "properties" : {
    "cat" : 5,
    "label" : "unimproved road"
  }
}, {
  "type" : "Feature",
...
```

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