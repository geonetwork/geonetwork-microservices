# Indexing service

This service demonstrates how to connect to the GeoNetwork database.

Database configuration is loaded from the configuration service.

Start the service using:
```
mvn spring-boot:run
```



http://localhost:9997/actuator/prometheus
http://localhost:9997/index/all

## Metrics

```
gn_index_totalNumberOfRecordsToIndex 0.0
gn_index_totalNumberOfRecordsIndexed_records_total{gn="index",} 356.0

gn_index_task_771df89e_7e86_4cc5_895a_2956509c302c_numberOfRecordsIndexed_records_total{gn="index",} 89.0
```
