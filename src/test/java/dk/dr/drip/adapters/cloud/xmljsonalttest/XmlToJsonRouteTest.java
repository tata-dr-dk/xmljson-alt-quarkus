package dk.dr.drip.adapters.cloud.xmljsonalttest;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class XmlToJsonRouteTest extends CamelQuarkusTestSupport {

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
    void test() throws InterruptedException {
        MockEndpoint flowEndpoint = getMockEndpoint("mock:result");

        // test valid flow messages
        flowEndpoint.reset();
        flowEndpoint.setExpectedMessageCount(1);
        template.sendBody("direct:flow-publication-converter", getXmlMessage());
        flowEndpoint.assertIsSatisfied();
    }

    private String getXmlMessage() {
        return "<flowPublicationMessage><action>CU</action><version>14</version><modifiedTime>2023-10-09T13:23:59.677</modifiedTime><production><number>00921927550</number><type>TV</type></production><flowPublication><id>4655172920812</id><title>Stop! - Hule</title><description>Maja vil bygge en hyggelig hule, men hendes værelse er alt for lille. I stuen er der masser af plads og ting at bygge med. Men farmor skal komme på besøg, og far vil have at huset skal være opryddet. Han bliver ikke glad, da han opdager Majas store planer.</description><distributor>Sveriges Television AB</distributor><broadcastDate>2023-10-22</broadcastDate><transmissionStartTime>2023-10-22T10:40:00.000</transmissionStartTime><transmissionEndTime>2023-10-22T10:47:04.360</transmissionEndTime><channelPresentationName>DR Ramasjang</channelPresentationName><channelPresentationCode>TVR</channelPresentationCode><live>false</live><rerun>true</rerun><technicalSubtextType>UTXT</technicalSubtextType><numberOfBlocks>1</numberOfBlocks><quickReprise>false</quickReprise><videoAspectRatio>16:9</videoAspectRatio><streamingLive>false</streamingLive><streamingOd>false</streamingOd><reconciled>false</reconciled><blocks><id>4655172921813</id><description>Maja vil bygge en hyggelig hule, men hendes værelse er alt for lille. I stuen er der masser af plads og ting at bygge med. Men farmor skal komme på besøg, og far vil have at huset skal være opryddet. Han bliver ikke glad, da han opdager Majas store planer.</description><partNumber>1</partNumber><broadcastDate>2023-10-22</broadcastDate><startTimeAnnounced>2023-10-22T10:40:00.000</startTimeAnnounced><startTimeSlot>2023-10-22T10:40:00.000</startTimeSlot><stopTimeSlot>2023-10-22T10:47:04.360</stopTimeSlot><printable>false</printable><epgrerun>false</epgrerun><timeAllocations><id>4952695963321</id><type>Auto promotion</type><events><id>4952700839327</id><title>IDR: Hr. Skæg &amp; Staffan ABC </title><startTimePresentation>2023-10-22T10:38:37.040</startTimePresentation><stopTimePresentation>2023-10-22T10:38:53.040</stopTimePresentation><live>false</live><videoAspectRatio>16:9</videoAspectRatio><audio>Stereo</audio><production><number>00331400270</number></production></events><events><id>4967311745327</id><title>TRR: App opdatering Dans eller dø</title><startTimePresentation>2023-10-22T10:38:53.040</startTimePresentation><stopTimePresentation>2023-10-22T10:39:19.040</stopTimePresentation><live>false</live><videoAspectRatio>16:9</videoAspectRatio><audio>Stereo</audio><production><number>00552204280</number></production></events><events><id>4967311747327</id><title>TRR: Dino Ranch og Gigantosaurus lige nu i Ramasjang på DRTV (0000000014791)</title><startTimePresentation>2023-10-22T10:39:19.040</startTimePresentation><stopTimePresentation>2023-10-22T10:39:49.040</stopTimePresentation><live>false</live><videoAspectRatio>16:9</videoAspectRatio><audio>Stereo</audio><production><number>00532300620</number></production></events><events><id>4967311583327</id><title>PRR: NU Stop! break</title><startTimePresentation>2023-10-22T10:39:49.040</startTimePresentation><stopTimePresentation>2023-10-22T10:39:56.040</stopTimePresentation><live>false</live><videoAspectRatio>16:9</videoAspectRatio><audio>Stereo</audio><production><number>00331703480</number></production></events></timeAllocations><timeAllocations><id>4655172926321</id><type>Segment of program</type><events><id>4655172927327</id><title>Stop! - Hule</title><startTimePresentation>2023-10-22T10:40:00.000</startTimePresentation><stopTimePresentation>2023-10-22T10:47:04.360</stopTimePresentation><live>false</live><videoAspectRatio>16:9</videoAspectRatio></events></timeAllocations></blocks><mediaAsset><id>0032491</id><preferred>true</preferred><canBeUsedForBroadcasting>true</canBeUsedForBroadcasting><element><id>id urn:dr:ma:mob:17168244</id><sequenceNumber>1</sequenceNumber><type>audio</type><duration>\n" +
                "        PT00H07M04.09S\n" +
                "    </duration><rfb>true</rfb><audioMode>Original lyd</audioMode><mediearkivUrn>urn:dr:ma:mob:17168244</mediearkivUrn></element><element><id>SUB 010203</id><sequenceNumber>1</sequenceNumber><type>subtitle</type><duration>\n" +
                "        PT00H07M04.09S\n" +
                "    </duration><rfb>true</rfb><subtitlingMode>UTXT</subtitlingMode></element><element><id>id urn:dr:ma:mob:17168244</id><sequenceNumber>1</sequenceNumber><type>video</type><duration>\n" +
                "        PT00H07M04.09S\n" +
                "    </duration><rfb>true</rfb><mediearkivUrn>urn:dr:ma:mob:17168244</mediearkivUrn><aspectRatioCode>16:9</aspectRatioCode><aspectRatioText>HD</aspectRatioText><aspectRatioPrintcode>16:9</aspectRatioPrintcode></element><expression>Default</expression></mediaAsset></flowPublication></flowPublicationMessage>";
    }

    private String getGcpFlowPublicationOutMessage() {
        return "{\n" +
                "  \"action\" : \"CU\",\n" +
                "  \"version\" : \"14\",\n" +
                "  \"modifiedTime\" : \"2023-10-09T15:23:59.677+02:00\",\n" +
                "  \"production\" : {\n" +
                "    \"number\" : \"00921927550\",\n" +
                "    \"type\" : \"TV\"\n" +
                "  },\n" +
                "  \"flowPublication\" : {\n" +
                "    \"id\" : \"4655172920812\",\n" +
                "    \"title\" : \"Stop! - Hule\",\n" +
                "    \"description\" : \"Maja vil bygge en hyggelig hule, men hendes værelse er alt for lille. I stuen er der masser af plads og ting at bygge med. Men farmor skal komme på besøg, og far vil have at huset skal være opryddet. Han bliver ikke glad, da han opdager Majas store planer.\",\n" +
                "    \"distributor\" : \"Sveriges Television AB\",\n" +
                "    \"broadcastDate\" : \"2023-10-22\",\n" +
                "    \"transmissionStartTime\" : \"2023-10-22T12:40:00.000+02:00\",\n" +
                "    \"transmissionEndTime\" : \"2023-10-22T12:47:04.360+02:00\",\n" +
                "    \"channelPresentationName\" : \"DR Ramasjang\",\n" +
                "    \"channelPresentationCode\" : \"TVR\",\n" +
                "    \"live\" : \"false\",\n" +
                "    \"rerun\" : \"true\",\n" +
                "    \"technicalSubtextType\" : \"UTXT\",\n" +
                "    \"numberOfBlocks\" : \"1\",\n" +
                "    \"quickReprise\" : \"false\",\n" +
                "    \"videoAspectRatio\" : \"16:9\",\n" +
                "    \"streamingLive\" : \"false\",\n" +
                "    \"streamingOd\" : \"false\",\n" +
                "    \"reconciled\" : \"false\",\n" +
                "    \"blocks\" : [ {\n" +
                "      \"id\" : \"4655172921813\",\n" +
                "      \"description\" : \"Maja vil bygge en hyggelig hule, men hendes værelse er alt for lille. I stuen er der masser af plads og ting at bygge med. Men farmor skal komme på besøg, og far vil have at huset skal være opryddet. Han bliver ikke glad, da han opdager Majas store planer.\",\n" +
                "      \"partNumber\" : \"1\",\n" +
                "      \"broadcastDate\" : \"2023-10-22\",\n" +
                "      \"startTimeAnnounced\" : \"2023-10-22T12:40:00.000+02:00\",\n" +
                "      \"startTimeSlot\" : \"2023-10-22T12:40:00.000+02:00\",\n" +
                "      \"stopTimeSlot\" : \"2023-10-22T12:47:04.360+02:00\",\n" +
                "      \"printable\" : \"false\",\n" +
                "      \"epgrerun\" : \"false\",\n" +
                "      \"timeAllocations\" : [ {\n" +
                "        \"id\" : \"4952695963321\",\n" +
                "        \"type\" : \"Auto promotion\",\n" +
                "        \"events\" : [ {\n" +
                "          \"id\" : \"4952700839327\",\n" +
                "          \"title\" : \"IDR: Hr. Skæg & Staffan ABC\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:38:37.040+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:38:53.040+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\",\n" +
                "          \"audio\" : \"Stereo\",\n" +
                "          \"production\" : {\n" +
                "            \"number\" : \"00331400270\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"id\" : \"4967311745327\",\n" +
                "          \"title\" : \"TRR: App opdatering Dans eller dø\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:38:53.040+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:39:19.040+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\",\n" +
                "          \"audio\" : \"Stereo\",\n" +
                "          \"production\" : {\n" +
                "            \"number\" : \"00552204280\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"id\" : \"4967311747327\",\n" +
                "          \"title\" : \"TRR: Dino Ranch og Gigantosaurus lige nu i Ramasjang på DRTV (0000000014791)\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:39:19.040+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:39:49.040+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\",\n" +
                "          \"audio\" : \"Stereo\",\n" +
                "          \"production\" : {\n" +
                "            \"number\" : \"00532300620\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"id\" : \"4967311583327\",\n" +
                "          \"title\" : \"PRR: NU Stop! break\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:39:49.040+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:39:56.040+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\",\n" +
                "          \"audio\" : \"Stereo\",\n" +
                "          \"production\" : {\n" +
                "            \"number\" : \"00331703480\"\n" +
                "          }\n" +
                "        } ]\n" +
                "      }, {\n" +
                "        \"id\" : \"4655172926321\",\n" +
                "        \"type\" : \"Segment of program\",\n" +
                "        \"events\" : [ {\n" +
                "          \"id\" : \"4655172927327\",\n" +
                "          \"title\" : \"Stop! - Hule\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:40:00.000+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:47:04.360+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\"\n" +
                "        } ]\n" +
                "      } ]\n" +
                "    } ],\n" +
                "    \"mediaAsset\" : [ {\n" +
                "      \"id\" : \"0032491\",\n" +
                "      \"preferred\" : \"true\",\n" +
                "      \"canBeUsedForBroadcasting\" : \"true\",\n" +
                "      \"element\" : [ {\n" +
                "        \"id\" : \"id urn:dr:ma:mob:17168244\",\n" +
                "        \"sequenceNumber\" : \"1\",\n" +
                "        \"type\" : \"audio\",\n" +
                "        \"duration\" : \"PT00H07M04.09S\",\n" +
                "        \"rfb\" : \"true\",\n" +
                "        \"audioMode\" : \"Original lyd\",\n" +
                "        \"mediearkivUrn\" : \"urn:dr:ma:mob:17168244\"\n" +
                "      }, {\n" +
                "        \"id\" : \"SUB 010203\",\n" +
                "        \"sequenceNumber\" : \"1\",\n" +
                "        \"type\" : \"subtitle\",\n" +
                "        \"duration\" : \"PT00H07M04.09S\",\n" +
                "        \"rfb\" : \"true\",\n" +
                "        \"subtitlingMode\" : \"UTXT\"\n" +
                "      }, {\n" +
                "        \"id\" : \"id urn:dr:ma:mob:17168244\",\n" +
                "        \"sequenceNumber\" : \"1\",\n" +
                "        \"type\" : \"video\",\n" +
                "        \"duration\" : \"PT00H07M04.09S\",\n" +
                "        \"rfb\" : \"true\",\n" +
                "        \"mediearkivUrn\" : \"urn:dr:ma:mob:17168244\",\n" +
                "        \"aspectRatioCode\" : \"16:9\",\n" +
                "        \"aspectRatioText\" : \"HD\",\n" +
                "        \"aspectRatioPrintcode\" : \"16:9\"\n" +
                "      } ],\n" +
                "      \"expression\" : \"Default\"\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"touched\" : 1696857840391\n" +
                "}";
    }
    private String getWoCacheFlowPublicationMessage() {
        return "{\n" +
                "  \"action\" : \"CU\",\n" +
                "  \"version\" : \"14\",\n" +
                "  \"modifiedTime\" : \"2023-10-09T15:23:59.677+02:00\",\n" +
                "  \"production\" : {\n" +
                "    \"number\" : \"00921927550\",\n" +
                "    \"type\" : \"TV\"\n" +
                "  },\n" +
                "  \"flowPublication\" : {\n" +
                "    \"id\" : \"4655172920812\",\n" +
                "    \"title\" : \"Stop! - Hule\",\n" +
                "    \"description\" : \"Maja vil bygge en hyggelig hule, men hendes værelse er alt for lille. I stuen er der masser af plads og ting at bygge med. Men farmor skal komme på besøg, og far vil have at huset skal være opryddet. Han bliver ikke glad, da han opdager Majas store planer.\",\n" +
                "    \"distributor\" : \"Sveriges Television AB\",\n" +
                "    \"broadcastDate\" : \"2023-10-22\",\n" +
                "    \"transmissionStartTime\" : \"2023-10-22T12:40:00.000+02:00\",\n" +
                "    \"transmissionEndTime\" : \"2023-10-22T12:47:04.360+02:00\",\n" +
                "    \"channelPresentationName\" : \"DR Ramasjang\",\n" +
                "    \"channelPresentationCode\" : \"TVR\",\n" +
                "    \"live\" : \"false\",\n" +
                "    \"rerun\" : \"true\",\n" +
                "    \"technicalSubtextType\" : \"UTXT\",\n" +
                "    \"numberOfBlocks\" : \"1\",\n" +
                "    \"quickReprise\" : \"false\",\n" +
                "    \"videoAspectRatio\" : \"16:9\",\n" +
                "    \"streamingLive\" : \"false\",\n" +
                "    \"streamingOd\" : \"false\",\n" +
                "    \"reconciled\" : \"false\",\n" +
                "    \"blocks\" : [ {\n" +
                "      \"id\" : \"4655172921813\",\n" +
                "      \"description\" : \"Maja vil bygge en hyggelig hule, men hendes værelse er alt for lille. I stuen er der masser af plads og ting at bygge med. Men farmor skal komme på besøg, og far vil have at huset skal være opryddet. Han bliver ikke glad, da han opdager Majas store planer.\",\n" +
                "      \"partNumber\" : \"1\",\n" +
                "      \"broadcastDate\" : \"2023-10-22\",\n" +
                "      \"startTimeAnnounced\" : \"2023-10-22T12:40:00.000+02:00\",\n" +
                "      \"startTimeSlot\" : \"2023-10-22T12:40:00.000+02:00\",\n" +
                "      \"stopTimeSlot\" : \"2023-10-22T12:47:04.360+02:00\",\n" +
                "      \"printable\" : \"false\",\n" +
                "      \"epgrerun\" : \"false\",\n" +
                "      \"timeAllocations\" : [ {\n" +
                "        \"id\" : \"4952695963321\",\n" +
                "        \"type\" : \"Auto promotion\",\n" +
                "        \"events\" : [ {\n" +
                "          \"id\" : \"4952700839327\",\n" +
                "          \"title\" : \"IDR: Hr. Skæg & Staffan ABC\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:38:37.040+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:38:53.040+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\",\n" +
                "          \"audio\" : \"Stereo\",\n" +
                "          \"production\" : {\n" +
                "            \"number\" : \"00331400270\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"id\" : \"4967311745327\",\n" +
                "          \"title\" : \"TRR: App opdatering Dans eller dø\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:38:53.040+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:39:19.040+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\",\n" +
                "          \"audio\" : \"Stereo\",\n" +
                "          \"production\" : {\n" +
                "            \"number\" : \"00552204280\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"id\" : \"4967311747327\",\n" +
                "          \"title\" : \"TRR: Dino Ranch og Gigantosaurus lige nu i Ramasjang på DRTV (0000000014791)\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:39:19.040+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:39:49.040+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\",\n" +
                "          \"audio\" : \"Stereo\",\n" +
                "          \"production\" : {\n" +
                "            \"number\" : \"00532300620\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"id\" : \"4967311583327\",\n" +
                "          \"title\" : \"PRR: NU Stop! break\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:39:49.040+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:39:56.040+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\",\n" +
                "          \"audio\" : \"Stereo\",\n" +
                "          \"production\" : {\n" +
                "            \"number\" : \"00331703480\"\n" +
                "          }\n" +
                "        } ]\n" +
                "      }, {\n" +
                "        \"id\" : \"4655172926321\",\n" +
                "        \"type\" : \"Segment of program\",\n" +
                "        \"events\" : [ {\n" +
                "          \"id\" : \"4655172927327\",\n" +
                "          \"title\" : \"Stop! - Hule\",\n" +
                "          \"startTimePresentation\" : \"2023-10-22T12:40:00.000+02:00\",\n" +
                "          \"stopTimePresentation\" : \"2023-10-22T12:47:04.360+02:00\",\n" +
                "          \"live\" : \"false\",\n" +
                "          \"videoAspectRatio\" : \"16:9\"\n" +
                "        } ]\n" +
                "      } ]\n" +
                "    } ],\n" +
                "    \"mediaAsset\" : [ {\n" +
                "      \"id\" : \"0032491\",\n" +
                "      \"preferred\" : \"true\",\n" +
                "      \"canBeUsedForBroadcasting\" : \"true\",\n" +
                "      \"element\" : [ {\n" +
                "        \"id\" : \"id urn:dr:ma:mob:17168244\",\n" +
                "        \"sequenceNumber\" : \"1\",\n" +
                "        \"type\" : \"audio\",\n" +
                "        \"duration\" : \"PT00H07M04.09S\",\n" +
                "        \"rfb\" : \"true\",\n" +
                "        \"audioMode\" : \"Original lyd\",\n" +
                "        \"mediearkivUrn\" : \"urn:dr:ma:mob:17168244\"\n" +
                "      }, {\n" +
                "        \"id\" : \"SUB 010203\",\n" +
                "        \"sequenceNumber\" : \"1\",\n" +
                "        \"type\" : \"subtitle\",\n" +
                "        \"duration\" : \"PT00H07M04.09S\",\n" +
                "        \"rfb\" : \"true\",\n" +
                "        \"subtitlingMode\" : \"UTXT\"\n" +
                "      }, {\n" +
                "        \"id\" : \"id urn:dr:ma:mob:17168244\",\n" +
                "        \"sequenceNumber\" : \"1\",\n" +
                "        \"type\" : \"video\",\n" +
                "        \"duration\" : \"PT00H07M04.09S\",\n" +
                "        \"rfb\" : \"true\",\n" +
                "        \"mediearkivUrn\" : \"urn:dr:ma:mob:17168244\",\n" +
                "        \"aspectRatioCode\" : \"16:9\",\n" +
                "        \"aspectRatioText\" : \"HD\",\n" +
                "        \"aspectRatioPrintcode\" : \"16:9\"\n" +
                "      } ],\n" +
                "      \"expression\" : \"Default\"\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"touched\" : 1696857840391\n" +
                "}";
    }
}
