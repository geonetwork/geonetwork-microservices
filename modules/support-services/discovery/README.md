# Discovery service

The discovery service based on Netflix Eureka is taking care of service registration. While starting each service is registered in the discovery service and the gateway takes care of routing to services based on their registration. If multiple search services are registered in the discovery service, then the gateway can also take care of load balancing. 
