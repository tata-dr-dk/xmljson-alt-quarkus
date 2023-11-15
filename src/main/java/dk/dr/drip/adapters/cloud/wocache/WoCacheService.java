package dk.dr.drip.adapters.cloud.wocache;

import dk.dr.wo.cache.model.*;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api")
@RegisterRestClient(configKey = "wo-cache-api")
@RegisterClientHeaders(RequestAuthorizationHeaderFactory.class)
public interface WoCacheService {

    @Path("/flow-publications")
    @POST
    FlowPublicationMessage publishFlowPublication(String body);

    @Path("/flow-publications/{publicationId}")
    @GET
    FlowPublicationMessage getFlowPublicationById(@PathParam("publicationId") String id);

    @Path("/od-publications")
    @POST
    OdMessage publishOdPublication(String body);

    @Path("/productions")
    @POST
    ProductionMessage publishProduction(String body);

    @Path("/presentation-series")
    @POST
    PresentationSeriesMessage publishPresentationSeries(String body);

    @Path("/parent-presentation-series")
    @POST
    ParentPresentationSeriesMessage publishParentPresentationSeries(String body);

    // todo: check what to do with missing Channel wocache-client model object
    @Path("/channels")
    @POST
    Object publishChannel(String body);

}