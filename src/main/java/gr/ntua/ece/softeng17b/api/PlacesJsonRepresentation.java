package gr.ntua.ece.softeng17b.api;

import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import com.google.gson.stream.JsonWriter;
import com.google.gson.Gson;

import java.util.List;
import java.io.Writer;
import java.io.IOException;

import gr.ntua.ece.softeng17b.data.Place;

/**
 * {count:NUMBER,results:[list-of-places]}
 */
class PlacesJsonRepresentation extends WriterRepresentation {

	private final List<Place> places;

	PlacesJsonRepresentation(List<Place> places) {
		super(MediaType.APPLICATION_JSON);
		this.places = places;
	}

	@Override
	public void write(Writer w) throws IOException {

		Gson gson = new Gson();

		JsonWriter writer = new JsonWriter(w);

		writer.beginObject();			
		writer.name("count").value(places.size());
		writer.name("results");
		writer.beginArray();
		writer.flush(); //flush buffers to start sendind data to the user early
		for(Place place: places) {
			String placeJson = gson.toJson(place);
			writer.jsonValue(placeJson);
			writer.flush();
		}
		writer.endArray();
		writer.endObject();
	}
}