# Logging configuration

Slf4j is used as a logging facade, and by default the logs will be redirected
onto the standard output.


Create a topic in the class and use the log:
```java
@Slf4j(topic = "org.fao.geonet.indexing.tasks")
public class IndexingService {
    ...
    log.info(String.format(
             "Index %s removed.",
             index));
```


If one need to tweak the log level, it can be done by modifying the
`bootstrap.yml` file of the spring-boot application, e.g.:

```
logging.level:
  org.springframework: DEBUG
  org.hibernate.SQL: DEBUG
  org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

