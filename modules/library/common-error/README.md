## Error

Errors handling conventions:

* All errors are localized. Use `exception.properties` file:

```java
new IllegalArgumentException(messages.getMessage(
        "api.exception.record.notFound", new String[]{"abcd"},
        request.getLocale()))
```

And add in `src/main/resources/messages/(exception|api|view).properties` the corresponding property:

```properties
api.exception.record.notFound=No record could be found with id {0}.
```

* Errors return the error cause but also if possible options to solve the problem. eg. an invalid parameter based on an enum, error MUST return the possibilities.


* Errors can be returned as JSON/XML/HTML depending on `Accept` header:

```shell script
curl -v -H "Accept:application/json" -H "Accept-language:en" http://localhost:9902/errortestlocalized

curl -v -H "Accept:application/json" -H "Accept-language:fr" http://localhost:9902/errortestlocalized

curl -v -H "Accept:application/xml" -H "Accept-language:fr" http://localhost:9902/errortestlocalized

curl -v -H "Accept:text/html" -H "Accept-language:fr" http://localhost:9902/errortestlocalized
```

