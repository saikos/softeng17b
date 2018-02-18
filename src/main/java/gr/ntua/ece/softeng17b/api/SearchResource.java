package gr.ntua.ece.softeng17b.api;

import gr.ntua.ece.softeng17b.conf.Configuration;
import gr.ntua.ece.softeng17b.data.DataAccess;
import gr.ntua.ece.softeng17b.data.SearchResults;
import gr.ntua.ece.softeng17b.data.SimpleLocation;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class SearchResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Form form = new Form(entity);

        String text        = form.getFirstValue("title");
        String subject     = form.getFirstValue("subject");
        String distance    = form.getFirstValue("distance");
        String latitude    = form.getFirstValue("latitude");
        String longitude   = form.getFirstValue("longitude");

        Long subjectLong   = parseLongOrReturnNull(subject);
        Long distanceLong  = parseLongOrReturnNull(distance);
        Double lat         = parseDoubleOrReturnNull(latitude);
        Double lon         = parseDoubleOrReturnNull(longitude);

        SearchResults results = dataAccess.searchEvents(
            text,
            subjectLong,
            distanceLong,
            lat == null || lon == null ? null : new SimpleLocation(lat, lon),
            0,
            10
        );

        return new IDsJsonRepresentation(results.ids.toArray(new String[results.ids.size()]));
    }

    public static Long parseLongOrReturnNull(String s) {
        try {
            return Long.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static Double parseDoubleOrReturnNull(String s) {
        try {
            return Double.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }
}
