# Configuration service

The configuration service centralized the configuration for all services with multiple profiles (dev, local, test, prod, ...). While starting each service is requesting their config from it. See [config](../../../config) folder at the root of this repository.

It uses a file storage for storing the configuration for all services.


Configuration can be retrieved using entry point like http://localhost:9999/geonetwork-dev.json.

Configuration can be refreshed on all microservices connected to the bus using:

```shell script
curl -X POST 127.0.0.1:9999/actuator/bus-refresh
```
