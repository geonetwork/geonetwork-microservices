# OGC API Record service

Build the OpenApi using `openapi-generator-maven-plugin`

```
mvn clean compile
```

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
```


Test the service:

```shell script

curl 127.0.0.1:9991/collections \
        -H "Accept: application/json"

curl 127.0.0.1:9991/collections \
        -H "Accept: text/html" -H "Accept-Language: fr"

curl 127.0.0.1:9991/collections.l=fr \
        -H "Accept: text/html"

firstCollection=$( \
curl 127.0.0.1:9991/collections \
        -H "Accept: application/json" \
         | jq -r '.collections[0].name')

curl 127.0.0.1:9991/collections/$firstCollection \
        -H "Accept: application/json"

curl 127.0.0.1:9991/collections/$firstCollection/sortables \
        -H "Accept: application/json"

curl 127.0.0.1:9991/collections/$firstCollection/items \
        -H "Accept: application/json" 

curl 127.0.0.1:9991/collections/$firstCollection/items \
        -H "Accept: application/xml" 

uuid=$( \
    curl 127.0.0.1:9991/collections/$firstCollection/items \
                 -H "Accept: application/json"  \
        | jq -r '.hits.hits[0]._id')

curl 127.0.0.1:9991/collections/$firstCollection/items/$uuid \
                 -H "Accept: application/json" 

curl 127.0.0.1:9991/collections/$firstCollection/items/$uuid \
                 -H "Accept: application/xml"
 
curl 127.0.0.1:9991/collections/$firstCollection/items/$uuid \
                 -H "Accept: application/gn-dcat 
```

API also `f` URL parameter to set the output format eg. http://localhost:9991/collections?f=xml



Start as a standalone module:

```shell script
mvn package
SERVER_PORT=9901 java -Dspring.profiles.active=standalone -jar target/gn-ogc-api-records.jar 

# With custom configuration
SERVER_PORT=9901 java -Dspring.profiles.active=standalone  -Dspring.config.location=./config/ -jar target/gn-ogc-api-records.jar
```