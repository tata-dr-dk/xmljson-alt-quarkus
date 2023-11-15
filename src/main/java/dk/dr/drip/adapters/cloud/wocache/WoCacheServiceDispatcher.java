package dk.dr.drip.adapters.cloud.wocache;

import dk.dr.wo.cache.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/*
 This could be used as a bean in the route to dispatch calls to the wo-cache backend.
 */
@ApplicationScoped
public class WoCacheServiceDispatcher {

    @RestClient
    WoCacheService woCacheService;

    public FlowPublicationMessage publishFlowPublication(String body) {
        return woCacheService.publishFlowPublication(body);
    }

    public OdMessage publishOdPublication(String body) {
        return woCacheService.publishOdPublication(body);
    }

    public ProductionMessage publishProduction(String body) {
        return woCacheService.publishProduction(body);
    }

    public PresentationSeriesMessage publishPresentationSeries(String body) {
        return woCacheService.publishPresentationSeries(body);
    }

    public ParentPresentationSeriesMessage publishParentPresentationSeries(String body) {
        return woCacheService.publishParentPresentationSeries(body);
    }

    public Object publishChannel(String body) {
        return woCacheService.publishChannel(body);
    }

}
