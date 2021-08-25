# Jackson-databind module for Data Functions

[Jackson-databind](https://github.com/FasterXML/jackson-databind) integration
for Data Functions.

Provides a Jackson module to read and write Data Functions domain model objects.

- `GeoRecord` is represented as a GeoJSON `Feature`.
- `Geometry` properties as GeoJSON `geometry`.

Using this project as a dependency in a Spring-boot application will automatically 
enable the Jackson module through Spring-boot auto-configuration.

