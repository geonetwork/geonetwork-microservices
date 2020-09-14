# GeoNetwork opensource services

This repository is a playground for experimenting GeoNetwork services focused on specific tasks.

Currently, the services are focused on:

* [Configuration](configuring/README.md)
* [Authorizing by using JWT](authorizing/README.md)
* [Searching](authorizing/README.md)
* [Routing](routing/README.md)
* [OGC API Records](ogc-api-records/README.md)


First start the configuration service, then the others.

![Start services](doc/img/springboot-services-start.png)


Test the service using the token:

```shell script
# Authenticate
gn_token=$( \
    curl '127.0.0.1:9998/authenticate' \
        --header "Content-Type: application/json" \
        --request POST \
        --data '{"username":"momo","password":"password"}' \
        | jq -r '.token')

# Search using the token
gn_auth_header=$(echo "Authorization: Bearer $gn_token")
curl 127.0.0.1:9990/search -H "$gn_auth_header"
```

Other areas to experiment:
* OGC API Records
* Dependency on GN domain module
* Indexing machine
* Harvesting machine
* ...
