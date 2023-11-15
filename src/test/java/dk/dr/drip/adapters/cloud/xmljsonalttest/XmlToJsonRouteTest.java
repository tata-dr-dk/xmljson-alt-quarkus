package dk.dr.drip.adapters.cloud.xmljsonalttest;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        runTestcase("production1", "administrativeTitle");
        runTestcase("production2", "administrativeTitle");
    }

    @Test
    void testFlowPublication() throws Exception {
        runTestcase("flowPublication1", "touched");
        runTestcase("flowPublication2", "touched");
    }

    @Test
    void testOdPublication() throws Exception {
        runTestcase("odPublication1", "previousStartTime");
        runTestcase("odPublication2", "previousStartTime");
    }

    @Test
    void testPresentationSeries() throws Exception {runTestcase("parentSeries1", "touched");
        runTestcase("series1", "touched");
        runTestcase("series2", "touched");
    }


    @Test
    void testParentPresentationSeries() throws Exception {
        runTestcase("parentSeries1", "touched");
        runTestcase("parentSeries2", "touched");
    }

    @Test
    void testChannel() {
        // todo: find channel examples. Not found from Audit
        //  (very rarely a channel gets created/updated in WO)
        Assertions.assertTrue(true);
    }

    private void runTestcase(String testcase, String truncateResultFromTag) throws Exception {
        MockEndpoint flowEndpoint = getMockEndpoint("mock:result");
        flowEndpoint.reset();

        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage(testcase));
        flowEndpoint.assertIsSatisfied();
        String outMessage = flowEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        String expected = getTruncatedJsonMessage(testcase, truncateResultFromTag);
        if(! outMessage.contains(expected)) {
            Assertions.assertEquals(expected, outMessage, "Result ("+testcase+") not as expected - check why");
        }
    }

    // *****************
    //      helpers
    // *****************
    private String getTruncatedJsonMessage(String testcase, String fieldToTruncateFrom) throws IOException {
        String json = getJsonMessage(testcase);
        int index = json.indexOf(fieldToTruncateFrom) - 10; // remove the "touched" field added by wo-cache-in
        return json.substring(0, index);
    }

    private String getJsonMessage(String testcase) throws IOException {
        return getMessage(testcase + ".json");
    }
    private String getXmlMessage(String testcase) throws IOException {
        return getMessage(testcase + ".xml");
    }
    private String getMessage(String testcaseFilename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(TEST_FILES_PATH + testcaseFilename)));
    }

}
