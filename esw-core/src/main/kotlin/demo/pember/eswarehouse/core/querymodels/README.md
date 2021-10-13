# Query Models

This package contains interfaces to be implemented by the core app
to support various Queries and Query Models that our services will support.

While the Event Journal _can_ answer all questions for our service, it is generally
not very efficient for complex reads. Even something as simple as 'find me all active instances
of type x' would require reading the current state of all Entities.

That being said, these Query Models don't necessarily have to be database table(s) either, if the query model
has a finite addressable space (e.g. a list of all currently sold Skus, current warehouse inventory, daily sales
snapshots), the Model could be an in memory object. The service could - on startup - read all
events from the journal, build up its models, then signal it has completed startup. This approach would 
require that the service emit to itself all events to ensure each instance is up-to-date, however