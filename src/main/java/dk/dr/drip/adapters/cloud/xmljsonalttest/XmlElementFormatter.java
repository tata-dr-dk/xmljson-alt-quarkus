package dk.dr.drip.adapters.cloud.xmljsonalttest;

import io.quarkus.runtime.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public class XmlElementFormatter {
    private static final ZoneOffset DEFAULT_INPUT_ZONE_OFFSET = ZoneOffset.UTC;
    private static final DateTimeFormatter LENIENT_PARSER = DateTimeFormatter.ISO_DATE_TIME.withResolverStyle(ResolverStyle.LENIENT);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final Formatter dateFormatter = XmlElementFormatter::formatDates;
    public static final Formatter stringFormatter = XmlElementFormatter::formatStrings;

    private final List<String> pathsContainingStringsToTrim;
    private final List<String> pathsContainingDates;

    public XmlElementFormatter(List<String> pathsContainingStringsToTrim, List<String> pathsContainingDates) {
        this.pathsContainingStringsToTrim = pathsContainingStringsToTrim;
        this.pathsContainingDates = pathsContainingDates;
    }

    public Document format(Document input) throws XPathExpressionException {
        for (String path : pathsContainingDates) {
            eval(input, path, dateFormatter);
        }
        for (String path : pathsContainingStringsToTrim) {
            eval(input, path, stringFormatter);
        }
        return input;
    }

    private void eval(Document doc, String path, Formatter formatter) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList myNodeList = (NodeList)xPath.compile(path).evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < myNodeList.getLength(); i++) {
            String startTime = myNodeList.item(i).getTextContent();
            myNodeList.item(i).setTextContent(formatter.format(startTime));
        }
    }

    private static String formatStrings(final String string) {
        if(StringUtil.isNullOrEmpty(string)) {
            return string;
        }
        return string.replaceAll("\\n\\s+","\n");
    }

    private static String formatDates(final String date) {
        try {
            TemporalAccessor accessor = toUtc(LENIENT_PARSER.parse(date));
            return FORMATTER.format(accessor);
        } catch (DateTimeParseException e) {
            // date not in ISO_DATE_TIME format. Return input unchanged.
        }
        return date;
    }

    private static TemporalAccessor toUtc(TemporalAccessor parsedTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.from(parsedTime);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(localDateTime, DEFAULT_INPUT_ZONE_OFFSET, zoneId);
        return zonedDateTime.withZoneSameLocal(ZoneId.of("Europe/Copenhagen"));
    }

    public interface Formatter {
        String format(String param2);
    }
}
