package gr.ntua.ece.softeng17b.api;

import com.google.gson.stream.JsonWriter;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

class IDsJsonRepresentation extends WriterRepresentation {

    private final String[] ids;

    IDsJsonRepresentation(String... ids) {
        super(MediaType.APPLICATION_JSON);
        this.ids = ids;
    }

    @Override
    public void write(Writer w) throws IOException {

        JsonWriter writer = new JsonWriter(w);

        writer.beginObject();
        writer.name("ids");
        writer.beginArray();
        for(String id : ids) {
            writer.value(id);
        }
        writer.endArray();
        writer.endObject();
    }
}
