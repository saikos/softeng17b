## Support for ElasticSearch

### Prerequisites

We use the latest 6.x version of ElasticSearch (ES).

1. Download the ES server from [here](https://www.elastic.co/downloads/elasticsearch).
2. Follow the instructions to start it up.

### ES client

We use the high-level Rest client to contact the ES server from our code. Documentation is available [here](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html).

The new `Elastic` class has been introduced to drive the communication with the ES server. This class performs the following operations:

* Creates the ES index, if required. The name of the index is defined in the `app.properties` file. During the creation of the index, an appropriate mapping (ES term for document type) is also setup, to handle the geo_point data type. This is rather convenient, as you are able to delete the ES index at any time (using any ES method of choice, e.g. through `curl -X DELETE http://localhost:9200/index`) and just restart the webapp to start over.
* Adds new events in the ES index.
* Provides the search entry-point (all searches are performed through a single method).

### Events

The sample code introduces an Event class to model the events of the project's domain. 

The Event model is not complete but it covers all the cases required (free-text, event attribute value, geo-distance).

The `DataAccess` class has been updated to support the following:

* the addition of a new event. The method inserts the event in the MySQL database, it retrieves its newly created id (auto-increment) and as a last step, it adds the event to ES, performing an appropriate transformation to its attributes.
* the execution of a search, calling the respective `Elastic.search` method.

### Rest endpoints

Two Rest endpoints have been added to the webapp to demonstrate and test the use of the above.

* `EventsResource`: Mapped to the `/events` endpoint, it can be used to create new events.
* `SearchResource`: Mapped to the `/search` endpoint, it can be used to perform searches.

#### Create new event through a Rest call
```
curl -d "place=PLID&title=T&description=D&subject=SID&tickets=NUM" -X POST http://localhost:8765/app/api/events
```
where:

* PLID: The id of an existing place (the geo-point data will be retrieved from this).
* T: the title of the event.
* D: the description of the event.
* SID: the id of an existing subject (subjects are selected using a dropdown in the UI).
* NUM: the number of available tickets.

#### Search for events with a Rest call
```
curl -d "text=TXT&subject=SID&latitude=LAT&longitude=LON&distance=D" -X POST http://localhost:8765/app/api/search
```

where:
* TXT: a free text.
* SID: the id of an existing subject.
* LAT, LON: The latitude / longitude of the user's point of interest (e.g. her home).
* D: the distance.

None of the above are mandatory: if no search fields are defined, the code will return all the events that have tickets available.
