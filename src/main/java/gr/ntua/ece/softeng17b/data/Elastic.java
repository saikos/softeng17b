package gr.ntua.ece.softeng17b.data;

import com.google.gson.stream.JsonWriter;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Elastic {

    private RestHighLevelClient client;
    private String index;

    public void setup(String host, int port, String index) {
        try {
            //set the name of the index
            this.index = index;

            //create the high-level client
            client = new RestHighLevelClient(
                RestClient.builder(
                    new HttpHost(host, port, "http")
                )
            );

            if (!indexExists()) {
                //create the index using the appropriate event mapping
                CreateIndexRequest req = Requests.createIndexRequest(index).
                                         mapping("event", createEventMapping(), XContentType.JSON);
                CreateIndexResponse resp = client.indices().create(req);
                if (! resp.isAcknowledged()) {
                    throw new Exception("Index creation failed");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    void shutdown() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            //do nothing
        }
    }

    private boolean indexExists() throws Exception {
        //check that the index exists using the low-level client
        Response response = client.getLowLevelClient().performRequest("HEAD", "/" + index);
        return (response.getStatusLine().getStatusCode() == 200);
    }

    private String createEventMapping() throws Exception {
        //Return the event mapping in json.
        //We only define the location field type manually, since elastic can dynamically
        //determine the type of the other fields (strings, numbers)

        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.beginObject();
        writer.name("properties");
            writer.beginObject();
            writer.name("location");
                writer.beginObject();
                    writer.name("type").value("geo_point");
                writer.endObject();
            writer.endObject();
        writer.endObject();
        writer.close();

        return sw.toString();
    }

    void add(Event event) throws RuntimeException {
        try {
            //jsonify the event
            StringWriter sw = new StringWriter();
            JsonWriter writer = new JsonWriter(sw);
            writer.beginObject();
            writer.name("title").value(event.getTitle());
            writer.name("description").value(event.getDescription());
            writer.name("location");
                writer.beginObject();
                writer.name("lat").value(event.getPlace().getLatitude());
                writer.name("lon").value(event.getPlace().getLongitude());
                writer.endObject();
            writer.name("subject").value(event.getSubject());
            writer.name("hasTickets").value(event.getTicketsAvailable() > 0);
            writer.endObject();
            writer.close();

            IndexRequest req = Requests.indexRequest(index).
                                        id(String.valueOf(event.getId())).
                                        type("event").
                                        source(sw.toString(), XContentType.JSON);
            client.index(req);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    SearchResults search(String text, Long subject, boolean hasTickets, Long distanceInKm, Location fromLoc, int from, int count) {
        //A single search entry point is provided for all cases.
        //It uses the BoolQuery of elastic to apply all user-supplied constraints / filters (must = AND).
        //The constraint to return only events that have tickets available is automatically applied.

        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            //we always set the ticketsAvailable (empty query cannot occur)
            boolQuery.must(
                QueryBuilders.termQuery("hasTickets", hasTickets)
            );

            if (text != null) {
                boolQuery.must(
                    QueryBuilders.queryStringQuery(text) //search everywhere for the given text
                );
            }

            if (subject != null) {
                boolQuery.must(
                    QueryBuilders.termQuery("subject", subject)
                );
            }

            //handle all other possible user "filters" accordingly,
            //e.g. use a range query to handle age

            if (distanceInKm != null && fromLoc != null) {
                boolQuery.filter(
                    QueryBuilders.geoDistanceQuery("location").
                                  distance(distanceInKm, DistanceUnit.KILOMETERS).
                                  point(fromLoc.getLatitude(), fromLoc.getLongitude())
                );
            }

            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.from(from).size(count).query(boolQuery);

            SearchRequest req = Requests.searchRequest(index).source(builder);
            SearchResponse response = client.search(req);
            SearchHits searchHits = response.getHits();
            SearchHit[] hits = searchHits.getHits();
            List<String> ids = new ArrayList<>();
            for (SearchHit hit : hits) {
                ids.add(hit.getId());
            }
            return new SearchResults(searchHits.totalHits, from, hits.length, ids);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}
