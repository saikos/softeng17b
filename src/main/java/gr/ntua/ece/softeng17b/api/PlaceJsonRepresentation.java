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
 * {name/value pairs of Place fields/values}
 */
class PlaceJsonRepresentation extends WriterRepresentation {

	private final Place place;

	PlaceJsonRepresentation(Place place) {
		super(MediaType.APPLICATION_JSON);
		this.place = place;
	}

	@Override
	public void write(Writer w) throws IOException {

		Gson gson = new Gson();
		w.write(gson.toJson(place));
	}
}