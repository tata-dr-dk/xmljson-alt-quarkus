package dk.dr.drip.adapters.cloud.xmljsonalttest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import generated.ProductRegionsType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import java.io.IOException;
import java.util.TimeZone;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.w3c.dom.Document;

@ApplicationScoped
public class XmlToJsonRoute extends EndpointRouteBuilder {
    @Inject
    XmlElementFormatter dateFormatter;

    /*
        dateFormatter (utc etc.)
        production-in replace brackets with parenthesis
        validate against xsd
            onException -> dead-message-queue
        xmlhack to avoid empty lists as null etc.
        json convert
        flow-publication-in json: flowPublicationIdEnforcer
     */
    @Override
    public void configure() {
        // unmarshal xml
        JaxbDataFormat jaxb = new JaxbDataFormat("generated");

        // marshal json
        JacksonDataFormat dataFormat = new JacksonDataFormat();
        dataFormat.setObjectMapper(getObjectMapper());
        dataFormat.setPrettyPrint(true);

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


    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = JsonMapper.builder()
                // "1" instead of 1
                .enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
                .disable(SerializationFeature.INDENT_OUTPUT)
                .build()
                // preserve list types
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false)
                // remove null/empty lists elements in output
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                // dates
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));

        // booleanS as String, "false" instead of false
        SimpleModule module = new SimpleModule().addSerializer(Boolean.class, new JsonSerializer<>() {
            @Override
            public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString());
            }
        });
        module.addSerializer(generated.PlatformsType.class, new JsonSerializer<>() {
            @Override
            public void serialize(generated.PlatformsType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                String[] array = value.getPlatforms().toArray(new String[0]);
                gen.writeArray(array,0 ,array.length);
            }
        });
        module.addSerializer(generated.ProductRegionsType.class, new JsonSerializer<>() {
            @Override
            public void serialize(ProductRegionsType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // already inside an array [], writeString adds elements to the array
                for(String region : value.getProductRegions()) {
                    gen.writeString(region);
                }
            }
        });

        // Register the module with the ObjectMapper
        mapper.registerModule(module);

        return mapper;
    }

}
