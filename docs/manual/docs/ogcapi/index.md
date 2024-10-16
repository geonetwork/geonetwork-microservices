 
# GeoNetwork MicroServices OGCAPI-Records {#toc}


This service module implements the ([OGCAPI-Records](https://ogcapi.ogc.org/records)).

| Endpoint Name | Endpoint Location | Meaning |
| -------- | ------- | -------- |
| [Landing Page](landingpage) | /   |   Home Page        |
| [Conformance declaration](conformance) | /conformance   |   Conformance Documents      |
| [OpenAPI Documentation](openapi) | /openapi   |   OpenAPI (Swagger) description document     |
| [Record collections](record-collections) | /collections   |   List of Catalogs<br>(GN portals)        |
| [Record collection](record-collection) | /collections/{collectionId}   |   Information about a single Catalog <br>(GN portal)       |
| [Records](records) | /collections/{collectionId}/items   |   Records in a Catalog<br>Search, etc...        |
| [Record](record) | /collections/{collectionId}/items/{recordId}   |   Single Metadata Record        |