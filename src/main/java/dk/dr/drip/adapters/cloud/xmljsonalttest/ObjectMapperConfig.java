package dk.dr.drip.adapters.cloud.xmljsonalttest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import generated.ProductRegionsType;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;
import org.apache.camel.Configuration;

import java.io.IOException;
import java.util.TimeZone;

@Configuration
@Singleton
class ObjectMapperConfig implements ObjectMapperCustomizer {

    public void customize(ObjectMapper mapper) {
        mapper
                // "1" instead of 1
                    // deprecated but no way this can be set with the non deprecated
                    // (JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS ) without building
                    // a new mapper, thus discarding the existing one.
                .enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS)
                .disable(SerializationFeature.INDENT_OUTPUT)
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
        }).addSerializer(generated.PlatformsType.class, new JsonSerializer<>() {
            @Override
            public void serialize(generated.PlatformsType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                String[] array = value.getPlatforms().toArray(new String[0]);
                gen.writeArray(array,0 ,array.length);
            }
        }).addSerializer(generated.ProductRegionsType.class, new JsonSerializer<>() {
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
    }
}
