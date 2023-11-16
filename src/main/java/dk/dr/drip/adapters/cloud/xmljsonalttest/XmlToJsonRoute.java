package dk.dr.drip.adapters.cloud.xmljsonalttest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import org.apache.camel.component.jackson.JacksonDataFormat;
import org.w3c.dom.Document;

@ApplicationScoped
public class XmlToJsonRoute extends EndpointRouteBuilder {

    @Inject
    JaxbDataFormat jaxb;

    @Inject
    JacksonDataFormat dataFormat;

    @Inject
    XmlElementFormatter dateFormatter;

    /*
    From program-cache-manager, here are the features to be replicated
        Done:
            dateFormatter (utc etc.)
            xmlhack to avoid empty lists as null etc.
            json convert
        Missing:
            production-in xml: replace brackets [] with parenthesis ()
            validate against xsd
                onException -> dead-message-queue
            flow-publication-in json: flowPublicationIdEnforcer
     */
    @Override
    public void configure() {
        from(direct("flow-publication-converter")).routeId("flow-publication-route")
                .log("in: \n${body}\n")
                .process(exchange -> {
                    Document body = exchange.getIn().getBody(Document.class);
                    exchange.getIn().setBody(dateFormatter.format(body));
                })
                //.bean(dateFormatter, "formatDates")
                .log("after format before unmarshal: \n${body}\n")
                .unmarshal(jaxb)
                .log("after unmarshal before marshal: \n${body}\n")
                .marshal(dataFormat)
                .log("out: \n${body}\n")
                .to(file("target/test-output-files")).id("flow-last-endpoint")

                .end();

        from(direct("clear-http-headers"))
                .removeHeader("Content-Length")
                .removeHeader("transfer-encoding")
                .removeHeader("X-Application-Context")
                .removeHeader("CamelHttpPath")
                .end();

    }

}
