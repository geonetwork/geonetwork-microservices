# Indexing service

This service demonstrates how to connect to the GeoNetwork database.

Database configuration is loaded from the configuration service.

Start the service using:
```
mvn spring-boot:run
```


```shell script
# Remove the index
curl '127.0.0.1:9997/api/index/records' \
    -X DELETE 

# Index all database records
curl '127.0.0.1:9997/api/index/all/all' \
   -X GET 

# Index one database record
curl '127.0.0.1:9997/api/index/simpleBatch/511' \
   -X GET 
```


http://localhost:9997/actuator/prometheus
http://localhost:9997/index/all

## Metrics

```
gn_index_totalNumberOfRecordsToIndex 0.0
gn_index_totalNumberOfRecordsIndexed_records_total{gn="index",} 356.0

gn_index_task_771df89e_7e86_4cc5_895a_2956509c302c_numberOfRecordsIndexed_records_total{gn="index",} 89.0
```
