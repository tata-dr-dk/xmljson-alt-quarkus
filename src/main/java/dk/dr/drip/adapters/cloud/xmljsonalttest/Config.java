package dk.dr.drip.adapters.cloud.xmljsonalttest;

import jakarta.enterprise.inject.Produces;
import org.apache.camel.Configuration;

import java.util.Arrays;

@Configuration
public class Config {

    @Produces
    public XmlDateFormatter getDateFormatter() {
        return new XmlDateFormatter(Arrays.asList(
                    "//odPublicationMessage/odPublication/startTime/text()"
                ,   "//odPublicationMessage/odPublication/endTime/text()"
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
}