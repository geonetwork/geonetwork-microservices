# Catalog.yaml definition {#toc}


See [Catalog Definition in the OGCAPI specification](https://docs.ogc.org/DRAFTS/20-004.html#req_records-api_catalog-response).

The properties come from the source's (portal sub-portal) in the GeoNetwork "sources" table, column "serviceRecord".  
This can be configured in the `GN GUI: Admin Console-Settings-sources` for sub-portals.

There is no GUI to set this for the "main" portal - do this in GN GUI: Admin Console-Settings-Settings-Catalog Service for the Web (CSW)-Record to use for GetCapabilities (`system/csw/capabilityRecordUuid`).


|Property|	Description | Elastic Index JSON Property |
|--------|	----------- | --------------------------- |
| id | A unique identifier for this catalog. | metadataIdentifier|
| created | The date this collection was created. | createDate|
| updated | The more recent date on which this collection was changed. |changeDate |
| conformsTo | The extensions/conformance classes used in this catalog object. | `not used` |
| type | Fixed value of "Catalog". | always "Catalog" |
| itemType | Fixed value of "record", "catalog" or both. | always "record"|
| title | A human-readable name given to this catalog. | resourceTitleObject|
| description | A free-text description of this catalog. | resourceAbstractObject|
| extent | The spatiotemporal coverage of this catalog. | spatial: geom <br> temporal: resourceTemporalDateRange  |
| crs | A list of coordinate reference systems used for spatiotemporal values. | coordinateSystem|
| keywords | A list of free-form keywords or tags associated with this collection. |tag |
themes | A knowledge organization system used to classify this collection. | allKeywords|
| language | The language used for textual values (i.e. titles, descriptions, etc.) of this collection object. |mainLanguage |
| languages | The list of other languages in which this collection object is available. |otherLanguage |
| recordLanguages | The list of languages in which records from the collection can be represented. |`not used` |
| contacts | A list of contacts qualified by their role(s). |contact |
| license | The legal provisions under which this collection is made available. |MD_LegalConstraintsUseLimitationObject |
| rights | A statement that concerns all rights not addressed by the license such as a copyright statement. | `not used`|
| recordsArrayName | The name of the array property in the catalog used to encode records in-line. The default value is records. | `not used`|
| records | An array of records encoded in-line in the catalog. |`not used` |
| links | A list of links related to this catalog. | `filled with Java code`|
| linkTemplates | A list of link templates related to this catalog. | `not used`|
| schemes | A list of schemes related to this catalog. |`not used`|

See `ElasticIndexJson2CollectionInfo.java` for more details.

NOTE: For `Contact.address`, we only fill in the `deliveryPoint` because that's all that's available in the Elastic Index JSON.

