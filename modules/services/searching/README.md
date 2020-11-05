# Search service

The main search service is an Elasticsearch proxy taking care of user privileges.

This main searcher is always in charge of searches and can be decorated to accept custom search API and custom response format. For this, 3 types of searcher can be defined and the following examples are provided:

1. Searcher returning index document as JSON (eg. main searcher)
1. Searcher returning index document as simple XML (eg. RSS searcher)
1. Searcher returning more elaborated response using XSLT on the complete metadata (eg. XsltSearchController)

Query as usually expressed using the Elasticsearch API but a searcher can customize this and take URL parameter and build the corresponding query (eg. RSS searcher).

The main searcher is the fastest option and does not require any database connection.


Remaining topics to work on:
* Require auth for some service or not 
* Retrieve user privileges from the token
* XSLT searcher / Plug formatter based on Accept header
* Define a strategy to deal with the selection mechanism (which needs to be shared across services and GeoNetwork default UI) 
* Adapt GeoNetwork 4 client to use the main search microservice (by custom routing in the gateway)
* Documentation on how to configure the Elasticsearch cluster 


Start the service with other microservices:
```shell script
docker-compose up -d elasticsearch rabbitmq discovery 
docker-compose up -d config
docker-compose up -d gateway auth
mvn spring-boot:run -Dspring-boot.run.profiles=dev,local -f modules/services/searching/
```


Start the service in standalone mode (see `bootstrap.yml` for the configuration) from the root project folder using:
```shell script
# Without security
SERVER_PORT=9902 mvn spring-boot:run -Dspring-boot.run.profiles=standalone,local -f modules/services/searching

# With security
SERVER_PORT=9902 mvn spring-boot:run -Dspring-boot.run.profiles=local -f modules/services/searching
```
In standalone mode, the service requires an Elasticsearch instance and database running.



Test the search:
```shell script

# RSS Search service 
curl 127.0.0.1:9902/portal/api/search/records/rss \
    -H "Accept: application/rss+xml"

# XSLT based search service 
# - Response in record format
curl 127.0.0.1:9902/portal/api/search/records/xslt \
    -H "Accept: application/gn-own" \
    -H "Content-type: application/json" \
    -X POST \
    -d '{"from": 0, "size": 1, "query": {"query_string": {"query": "+isTemplate:n"}}}'

# - Response in record DCAT format
curl 127.0.0.1:9902/portal/api/search/records/xslt \
    -H "Accept: application/gn-dcat" \
    -H "Content-type: application/json" \
    -X POST \
    -d '{"from": 0, "size": 1, "query": {"query_string": {"query": "+isTemplate:n"}}}'

# Elasticsearch service
curl 127.0.0.1:9902/portal/api/search/records/_search \
    -H "Accept: application/json" \
    -H "Content-type: application/json" \
    -X POST \
    -d '{"from": 0, "size": 10, "query": {"query_string": {"query": "+isTemplate:n"}}}' | jq -r '.hits.total.value'
```