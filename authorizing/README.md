# Authentication service

This service is in charge of checking user privileges using GeoNetwork user repository. Once authenticated, a JSON Web Token (JWT) is created. The token will contain necessary information about the user that may be required for other services to limit requests to the database (eg. user membership is required to build a search filter by the search application).


Start the service using:
```
mvn spring-boot:run
```

It use a memory auth provider with a single user `momo` with password `password`.

Test the service:
```shell script
curl '127.0.0.1:9998/authenticate' \
    --header "Content-Type: application/json" \
    --request POST \
    --data '{"username":"momo","password":"password"}'
```