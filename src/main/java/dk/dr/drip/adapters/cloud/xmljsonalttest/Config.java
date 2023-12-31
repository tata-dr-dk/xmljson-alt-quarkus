package dk.dr.drip.adapters.cloud.xmljsonalttest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.Produces;
import org.apache.camel.Configuration;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Configuration
public class Config {

    // ----------------------
    //  config xml unmarshal
    // ----------------------

    @Produces
    public XmlElementFormatter getXmlFormatter() {
        // in program-cache-manager, a similar dateFormatter was used, though each object was configured with a
        // separate list of XPaths/fields. Here we join all time related fields and apply to all business objects
        // assuming that no time field should be exempted, i.e. that it is not the case that a field like
        // 'createTime' should be formatter on od-publication but not flow publication
        // Also, whitespace trim-fields are added on the same formatter
        return new XmlElementFormatter(
                List.of("//description"),
                Arrays.asList(
                    "//odPublicationMessage/odPublication/startTime/text()"
                ,   "//odPublicationMessage/odPublication/endTime/text()"
                ,   "//broadcastTime/text()"
                ,   "//createTime/text()"
                ,   "//modifiedTime/text()"
                ,   "//transmissionStartTime/text()"
                ,   "//transmissionEndTime/text()"
                ,   "//startTimeAnnounced/text()"
                ,   "//startTimeSlot/text()"
                ,   "//stopTimeSlot/text()"
                ,   "//startTimePlayout/text()"
                ,   "//stopTimePlayout/text()"
                ,   "//startTimePresentation/text()"
                ,   "//stopTimePresentation/text()"
                ,   "//productionMessage/production/availableFrom/text()"
        ));
    }

    @Produces
    public JaxbDataFormat jaxb() {
        return new JaxbDataFormat("generated");
    }

    @Produces
    public JacksonDataFormat dataFormat(ObjectMapper objectMapper) {
        JacksonDataFormat dataFormat = new JacksonDataFormat(objectMapper, HashMap.class);
        dataFormat.setPrettyPrint(true);
        return dataFormat;
    }
}