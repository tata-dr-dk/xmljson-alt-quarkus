package dk.dr.drip.adapters.cloud.xmljsonalttest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import generated.FlowPublicationMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.xml.bind.JAXB;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.xml.catalog.Catalog;
import java.io.File;
import java.io.IOException;
import java.util.TimeZone;
import org.apache.camel.component.jackson.JacksonDataFormat;

@ApplicationScoped
public class XmlToJsonRoute extends EndpointRouteBuilder {

    @Override
    public void configure() {
        // unmarshal xml
        JaxbDataFormat jaxb = new JaxbDataFormat("generated");

        // marshal json
        JacksonDataFormat dataFormat = new JacksonDataFormat();
        dataFormat.setObjectMapper(getObjectMapper());
        dataFormat.setPrettyPrint(true);

        from(direct("flow-publication-converter")).routeId("flow-publication-route")
                .unmarshal(jaxb)
                .marshal(dataFormat)
                .to(file("target/test-output-files")).id("flow-last-endpoint")
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
        SimpleModule module = new SimpleModule().addSerializer(Boolean.class, new JsonSerializer<Boolean>() {
            @Override
            public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString());
            }
        });

        // Register the module with the ObjectMapper
        mapper.registerModule(module);

        return mapper;
    }

}
