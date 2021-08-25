# Data Functions API

Standard Java library to create data (geospatial and otherwise) pipelines 
using a functional programming style, in a framework agnostic way, using
only `java.util.function` and `java.util.stream` abstractions.

As such, the overall functionality is decomposed using high level constructs 
and `Supplier<>`, `Function<>`, and `Consumer<>` abstractions,
allowing the implementation of data producer, data transformation, and destination
targets as individual and composable components, agnostic of
the execution environment and deployment platforms.

The origin and destination of the streams of data to compose the processing
pipeline are also dependant on the execution environment rather
than hard coded, meaning data may come from files, HTTP POST request bodies, 
a cloud message broker such as an AMQP (Advanced Message Queuing Protocol)
or Kafka streams, and so on.
