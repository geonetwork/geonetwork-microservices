## Error

Errors are localized. Use:

```java
new IllegalArgumentException(messages.getMessage(
        "api.exception.record.notFound", new String[]{"abcd"},
        request.getLocale()))
```

And add in `src/main/resources/messages/(exception|api|view).properties` the corresponding property:

```properties
api.exception.record.notFound=No record could be found with id {0}.
```

Errors can be returned as JSON/XML/HTML depending on `Accept` header:


```shell script
curl -v -H "Accept:application/json" -H "Accept-language:en" http://localhost:9902/errortestlocalized

curl -v -H "Accept:application/json" -H "Accept-language:fr" http://localhost:9902/errortestlocalized

curl -v -H "Accept:text/html" -H "Accept-language:fr" http://localhost:9902/errortestlocalized
```

