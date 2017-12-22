package gr.ntua.ece.softeng17b.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestApi extends Application {

	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());

		//GET, POST
		router.attach("/places", PlacesResource.class);

		//GET, PUT, DELETE
		router.attach("/places/{id}", PlaceResource.class);

		return router;
	}

}