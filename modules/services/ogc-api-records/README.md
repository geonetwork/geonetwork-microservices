# OGC API Record service

## Prerequisite

Java >= 11
Maven >= 3.6.3

## Build

```console
mvn clean compile
```

## Run

### with spring-boot runner

#### run the service as part of the microservice architecture

Start the service using:

```console
mvn spring-boot:run
```

#### run the service as a standalone spring-boot app

> **_NOTE:_**  we need to have at least 2 running components
>
> - GeoNetwork database [check db configuration](https://github.com/geonetwork/geonetwork-microservices/blob/main/modules/services/ogc-api-records/src/main/resources/bootstrap.yml#L50)
> - Elasticsearch index [check index configuration](https://github.com/geonetwork/geonetwork-microservices/blob/main/modules/services/ogc-api-records/src/main/resources/bootstrap.yml#L75)

```console
mvn spring-boot:run -Dspring-boot.run.profiles=standalone
```

### run with custom port and profile

```console
SERVER_PORT=9901 mvn spring-boot:run -Dspring-boot.run.profiles=standalone
```

### Start as standalone service with a JAR

```console
mvn package
SERVER_PORT=9901 java -Dspring.profiles.active=standalone -jar target/gn-ogc-api-records.jar 

# With custom configuration
SERVER_PORT=9901 java -Dspring.profiles.active=standalone  -Dspring.config.location=./config/ -jar target/gn-ogc-api-records.jar
```

### Start as standalone service with a WAR

Use the `war` profile to build the WAR:

```shell script
cd modules/services/ogc-api-records
mvn package -Pwar,-docker

mvn jetty:run -Pwar -Dspring.profiles.active=standalone  -Dspring.config.location=./service/src/main/resources/
```

### Start as standalone service with docker

Build docker image from source:

```console
./mvnw clean install -Drelax
mkdir ogcapiconfig
cp modules/services/ogc-api-records/src/main/resources/bootstrap.yml ogcapiconfig/.
cp modules/library/common-search/src/main/resources/application.yml ogcapiconfig/.
# Adjust database and Elasticsearch connection info.
docker run -it -p8080:8080 \
  -v "`pwd`/ogcapiconfig:/ogcapiconfig/" \
  -e "SPRING_PROFILES_ACTIVE=standalone" \
  -e "SPRING_CONFIG_LOCATION=/ogcapiconfig/" \
  -e "JAVA_OPTS=-Dfile.encoding=UTF-8" \
  gn-cloud-ogc-api-records-service:4.2.8-0
```

or use a published release (create your configuration first like above):

```console
docker pull geonetwork/gn-cloud-ogc-api-records-service:0.1.0

docker run -it -p8080:8080   -v "`pwd`/ogcapiconfig:/ogcapiconfig/"   -e "SPRING_PROFILES_ACTIVE=standalone"   -e "SPRING_CONFIG_LOCATION=/ogcapiconfig/" geonetwork/gn-cloud-ogc-api-records-service:0.1.0
```

## Test the service

```console

# Collections
curl 127.0.0.1:9901/collections \
        -H "Accept: application/json"

curl 127.0.0.1:9901/collections \
        -H "Accept: text/html" -H "Accept-Language: fr"

curl 127.0.0.1:9901/collections?l=fr \
        -H "Accept: text/html"


# A collection
firstCollection=$( \
curl 127.0.0.1:9901/collections \
        -H "Accept: application/json" \
         | jq -r '.collections[0].name')

curl 127.0.0.1:9901/collections/$firstCollection \
        -H "Accept: application/json"

curl 127.0.0.1:9901/collections/$firstCollection \
        -H "Accept: application/opensearchdescription+xml"

curl 127.0.0.1:9901/collections/$firstCollection/sortables \
        -H "Accept: application/json"


# Collection records & search
curl 127.0.0.1:9901/collections/$firstCollection/items \
        -H "Accept: application/json" 

curl 127.0.0.1:9901/collections/$firstCollection/items \
        -H "Accept: application/xml" 

curl 127.0.0.1:9901/collections/$firstCollection/items \
        -H "Accept: application/rss+xml"

# Search parameters
# Use `limit` for the number of records per page
curl 127.0.0.1:9901/collections/$firstCollection/items?limit=20 \
        -H "Accept: application/json" 
## Should we add a max limit ?

# Use `startindex` for paging. Default is 0.
curl 127.0.0.1:9901/collections/$firstCollection/items?startindex=20&limit=20 \
        -H "Accept: application/json" 

# Full text search using `q`. Full text search is configurable in `application.yml` > `queryBase`
curl 127.0.0.1:9901/collections/$firstCollection/items?q=map \
        -H "Accept: application/json" 

# Full text search using `q`. Full text search is configurable in `application.yml` > `queryBase`
curl 127.0.0.1:9901/collections/$firstCollection/items?q=map \
        -H "Accept: application/json" 

# Sort by. Use 127.0.0.1:9901/collections/main/sortables to get the list of sortable fields
# Use + to sort in ascending order (default) or - for descending eg. -popularity
# Use multiple sortby parameters or a comma separated value eg. resourceTitleObject.default.keyword,-createDate
curl 127.0.0.1:9901/collections/$firstCollection/items?sortby=resourceTitleObject.default.keyword \
        -H "Accept: application/json" 

# Bbox search using `bbox` (relation: intersects)
curl 127.0.0.1:9901/collections/$firstCollection/items?bbox=-100,40,-80,50 \
        -H "Accept: application/json" 

# Search by record UUID using `externalids`
curl 127.0.0.1:9901/collections/$firstCollection/items?externalids=8306437bc59910b70223865b44100ffab97ba069&externalids=8a9bc9e8f86cb02be8be4450e310d261415ac909 \
        -H "Accept: application/json" 



# One record
uuid=$( \
    curl 127.0.0.1:9901/collections/$firstCollection/items \
                 -H "Accept: application/json"  \
        | jq -r '.hits.hits[0]._id')

curl 127.0.0.1:9901/collections/$firstCollection/items/$uuid \
                 -H "Accept: application/json" 

curl 127.0.0.1:9901/collections/$firstCollection/items/$uuid \
                 -H "Accept: application/ld+json" 

curl 127.0.0.1:9901/collections/$firstCollection/items/8108e203-59db-4672-b9e0-c1863fd6523b \
                 -H "Accept: application/ld+json" 

curl 127.0.0.1:9901/collections/$firstCollection/items/$uuid \
                 -H "Accept: application/xml"
 
curl 127.0.0.1:9901/collections/$firstCollection/items/$uuid \
                 -H "Accept: application/dcat2+xml 

curl 127.0.0.1:9901/collections/$firstCollection/items/$uuid \
        -H "Accept: text/turtle" 

curl 127.0.0.1:9901/collections/$firstCollection/items/$uuid \
        -H "Accept: application/rdf+xml" 
```

API also `f` URL parameter to set the output format eg. http://localhost:9901/collections?f=xml

## Customize your XSL

Run the following to update XSLT files while running the application:

```console
mvn process-resources

# TODO: Check how to update shared XSLT from other module with CLI? 
# Build module in Intellij works fine.
# cd modules/library/common-view; mvn compile
# Other workaround:
#cd modules/services/ogc-api-records/src/main/resources/xslt
#ln -s ../../../../../../../library/common-view/src/main/resources/xslt/core core
```

## Known issues

If the HTML page of an item return the following error

```console
org.xml.sax.SAXParseException; lineNumber: 28; columnNumber: 50; Invalid byte 2 of 3-byte UTF-8 sequence.
```

add `-Dfile.encoding=UTF-8`.
