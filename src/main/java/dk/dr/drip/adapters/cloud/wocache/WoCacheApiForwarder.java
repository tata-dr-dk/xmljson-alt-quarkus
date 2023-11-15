package dk.dr.drip.adapters.cloud.wocache;

import dk.dr.wo.cache.model.FlowPublicationMessage;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/*
 This is an alternative way to call the wo-cache backend through a local http api.
 Could be used instead of the bean (WoCacheServiceDispatcher).

    $ curl http://localhost:8080/forwarder/flow/4054751752812

 */
@Path("/forwarder")
public class WoCacheApiForwarder {

    @RestClient
    WoCacheService woCacheService;

    @GET
    @Path("/flow/{id}")
    public FlowPublicationMessage flowById(String id) {
        return woCacheService.getFlowPublicationById(id);
    }
}