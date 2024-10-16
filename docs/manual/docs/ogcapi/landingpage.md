# Landing Page {#toc}

The Landing Page will show information about the server.  It has links to the [Conformance declaration](conformance), the [OpenAPI Documentation](openapi), and the  [Record collections](record-collections).

See [Landing Page in the OGCAPI specification](https://docs.ogc.org/is/17-069r3/17-069r3.html#_api_landing_page).


## JSON Model

The JSON model follows the ["landingPage.yml"](https://schemas.opengis.net/ogcapi/features/part1/1.0/openapi/schemas/landingPage.yaml) definition in the OGCAPI Specification.

| JSON Tag|  Meaning |
| -------- | ------- | 
| title | Title for the server |
| description | Description of the server |
| links | Links to other documents |


The GeoNetwork OGCAPI-Records implementation also include a non-standard item:

| JSON Tag|  Meaning |
| -------- | ------- | 
| systemInfo | Metadata for the Server.  See the [catalog.yaml](../catalog) description. |


This metadata information is taken from the Service Record linked to the GeoNetwork's main portal.
