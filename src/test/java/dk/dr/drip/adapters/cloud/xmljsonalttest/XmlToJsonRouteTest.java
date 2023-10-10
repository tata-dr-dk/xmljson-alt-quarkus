package dk.dr.drip.adapters.cloud.xmljsonalttest;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

@QuarkusTest
class XmlToJsonRouteTest extends CamelQuarkusTestSupport {

    private static final String TEST_FILES_PATH = "src/test/data/";

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        AdviceWith.adviceWith(context, "flow-publication-route", a ->
                a.interceptSendToEndpoint(("file://target/test-output-files")).to("mock:result"));
    }


    @Test
    void testProduction() throws Exception {
        MockEndpoint flowEndpoint = getMockEndpoint("mock:result");
        String outMessage;

        // case 1
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("production1"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("production1"), outMessage);

        // case 2
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("production2"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("production2"), outMessage);
    }

    @Test
    void testFlowPublication() throws Exception {
        MockEndpoint flowEndpoint = getMockEndpoint("mock:result");
        String outMessage;
        // case 1
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("flowPublication1"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("flowPublication1"), outMessage);

        // case 2
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("flowPublication2"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("flowPublication2"), outMessage);
    }

    @Test
    void testOdPublication() throws Exception {
        MockEndpoint flowEndpoint = getMockEndpoint("mock:result");
        String outMessage;

        // case 1
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("odPublication1"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("odPublication1"), outMessage);

        // case 2
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("odPublication2"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("odPublication2"), outMessage);
    }

    @Test
    void testPresentationSeries() throws Exception {
        MockEndpoint flowEndpoint = getMockEndpoint("mock:result");
        String outMessage;

        // case 1
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("series1"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("series1"), outMessage);

        // case 2
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("series2"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("series2"), outMessage);
    }


    @Test
    void testParentPresentationSeries() throws Exception {
        MockEndpoint flowEndpoint = getMockEndpoint("mock:result");
        String outMessage;

        // case 1
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("parentSeries1"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("parentSeries1"), outMessage);

        // case 2
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("parentSeries2"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("parentSeries2"), outMessage);
    }


    //@Test
    void testChannel() throws Exception {
        MockEndpoint flowEndpoint = getMockEndpoint("mock:result");
        String outMessage;

        // case 1
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("odPublication1"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("odPublication1"), outMessage);

        // case 2
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage("odPublication2"));
        flowEndpoint.assertIsSatisfied();
        outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        Assertions.assertEquals(getJsonMessage("odPublication2"), outMessage);
    }


    private String getXmlMessage(String testcase) throws IOException {
        return getMessage(testcase + ".xml");
    }
    private String getJsonMessage(String testcase) throws IOException {
        return getMessage(testcase + ".json");
    }
    private String getMessage(String testcaseFilename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(TEST_FILES_PATH + testcaseFilename)));
    }

}
