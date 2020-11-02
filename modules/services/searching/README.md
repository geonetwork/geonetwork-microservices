# Search service

The Elasticsearch proxy (https://github.com/geonetwork/core-geonetwork/blob/4.0.x/services/src/main/java/org/fao/geonet/api/es/EsHTTPProxy.java) is extracted from GeoNetwork 4 and moved here to be the main search entry point. 

Challenges are:
* Try to remove dependencies on the database - we should be able to run a search without the need of the database (only user privileges retrieved by the authorizing app should be required and pass across the JWT)
* Define a strategy to deal with the selection mechanism (which needs to be shared across services and GeoNetwork default UI) 
* org.fao.geonet.index.es.EsRestClient can be replaced by the configuration service providing Elasticsearch URL

Once created, the GeoNetwork 4 client is improved to be able to use either the internal search service or the cloud service.

Documentation on how to configure the Elasticsearch cluster is written.


Start the service using:
```
mvn spring-boot:run
```

For now only valid the token created by the Authorizing app.


```shell script

docker-compose up -d elasticsearch rabbitmq discovery 
docker-compose up -d config
docker-compose up -d gateway auth
mvn spring-boot:run -Dspring-boot.run.profiles=dev,local -f modules/services/searching/


# RSS Search service 
curl 127.0.0.1:9902/portal/api/search/records/rss \
    -H "Accept: application/rss+xml" \
    -H "Content-type: application/xml" 


# Elasticsearch service
curl 127.0.0.1:9902/portal/api/search/records/_search \
    -H "Accept: application/json" \
    -H "Content-type: application/json" \
    -X POST \
    -d '{"from": 0, "size": 10, "query": {"query_string": {"query": "+isTemplate:n"}}}' | jq -r '.hits.total.value'
```