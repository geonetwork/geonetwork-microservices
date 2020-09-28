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

Test the service:

```shell script
curl '127.0.0.1:9991/collections/main' \
        --header "Content-Type: application/json"
```


Needs to be fixed:
* https://github.com/OpenAPITools/openapi-generator/issues/2901
```
java: package org.openapitools.jackson.nullable does not exist
```
