# Logging configuration

Slf4j is used as a logging facade, and by default the logs will be redirected
onto the standard output.

If one need to tweak the log level, it can be done by modifying the
`bootstrap.yml` file of the spring-boot application, e.g.:

```
logging.level:
  org.springframework: DEBUG
  org.hibernate.SQL: DEBUG
  org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

