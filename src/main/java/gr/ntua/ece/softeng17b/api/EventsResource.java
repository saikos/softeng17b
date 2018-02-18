package gr.ntua.ece.softeng17b.api;

import gr.ntua.ece.softeng17b.conf.Configuration;
import gr.ntua.ece.softeng17b.data.DataAccess;
import gr.ntua.ece.softeng17b.data.Event;
import gr.ntua.ece.softeng17b.data.Place;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.Optional;

public class EventsResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Form form = new Form(entity);

        String title        = form.getFirstValue("title");
        String description  = form.getFirstValue("description");
        String subject      = form.getFirstValue("subject");
        String tickets      = form.getFirstValue("tickets");
        String place        = form.getFirstValue("place");

        int ticketsAvailable;
        try {
            ticketsAvailable = Math.max(Integer.valueOf(tickets), 0);
        }
        catch (Exception e) {
            ticketsAvailable = 0;
        }

        long placeId;
        try {
            placeId = Long.valueOf(place);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid place id: " + place);
        }

        Optional<Place> optional = dataAccess.getPlace(placeId);
        Place p = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid place id: " + place));
        Event event = dataAccess.createEvent(p, title, description, SearchResource.parseLongOrReturnNull(subject), ticketsAvailable);
        return new IDsJsonRepresentation(String.valueOf(event.getId()));
    }
}
