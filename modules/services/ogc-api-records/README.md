# OGC API Record service

## Build

Build the OpenApi using `openapi-generator-maven-plugin`

```
mvn clean compile
```


## Start

Check the `target/generate-sources` folder.

Start the service using:
```
mvn spring-boot:run
```

or with custom port and profile:
```
SERVER_PORT=9901 mvn spring-boot:run -Dspring-boot.run.profiles=standalone
```

Run the following to update XSLT files while running the application:
```
mvn process-resources

# TODO: Check how to update shared XSLT from other module with CLI? 
# Build module in Intellij works fine.
# cd modules/library/common-view; mvn compile
# Other workaround:
#cd modules/services/ogc-api-records/service/src/main/resources/xslt
#ln -s ../../../../../../../library/common-view/src/main/resources/xslt/core core

```


## Test the service

```shell script

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
```

API also `f` URL parameter to set the output format eg. http://localhost:9901/collections?f=xml


## Start as standalone service

```shell script
mvn package
SERVER_PORT=9901 java -Dspring.profiles.active=standalone -jar target/gn-ogc-api-records.jar 

# With custom configuration
SERVER_PORT=9901 java -Dspring.profiles.active=standalone  -Dspring.config.location=./config/ -jar target/gn-ogc-api-records.jar
```


## Start as WAR

Use the `war` profile to build the WAR:

```shell script
cd modules/services/ogc-api-records/service
mvn package -Pwar,-docker

mvn jetty:run -Pwar -Dspring.profiles.active=standalone  -Dspring.config.location=./service/src/main/resources/
```