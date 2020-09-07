# Authentication service

Service creating a JWT to be used in other services.

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