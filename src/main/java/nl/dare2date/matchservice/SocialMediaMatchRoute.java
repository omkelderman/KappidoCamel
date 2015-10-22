package nl.dare2date.matchservice;

import nl.dare2date.kappido.services.MatchResponse;
import nl.dare2date.kappido.steam.SteamCamelAPIWrapper;
import nl.dare2date.kappido.twitch.TwitchCamelAPIWrapper;
import nl.dare2date.profile.FakeD2DProfileManager;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import java.util.Arrays;
import java.util.List;

public class SocialMediaMatchRoute extends RouteBuilder {
    public static final String HEADER_KAPPIDO_USER_ID = "KappidoUserId";
    public static final String HEADER_KAPPIDO_WEIGHTING = "KappidoWeighting";
    public static final String HEADER_KAPPIDO_MATCH_TYPE = "KappidoMatchType";

    @Override
    public void configure() throws Exception {
        JaxbDataFormat jaxbMatchResponse = new JaxbDataFormat(MatchResponse.class.getPackage().getName());
        Namespaces ns = new Namespaces("mes", "http://www.han.nl/schemas/messages");

//        from("spring-ws:rootqname:{http://www.han.nl/schemas/messages}MatchRequest?endpointMapping=#matchEndpointMapping")
//        .setExchangePattern(ExchangePattern.InOut)
//        .split(ns.xpath("//mes:user/*"), new SocialMediaMatchAggregrate()) // split the request into four separate parts
//            .parallelProcessing() // fork
//            .convertBodyTo(String.class) // we'll handle with the request as XML using XPath
//            .choice()
//                .when(body().contains("twitterName")) // send the twitterName to twitter and wait for the aggregrate to do something useful
//                    .log("Twitter: ${body}")
//                    .setHeader(TwitterConstants.TWITTER_KEYWORDS, ns.xpath("/mes:twitterName/text()", String.class)) // fill the keyword parameter
//                //.to("twitter://search")
//                .to("restlet:http://jsonplaceholder.typicode.com/posts")
//                //.otherwise() // send the facebookid to FB and wait for the aggregrate to do something useful
//                //.setHeader("CamelFacebook.userId", ns.xpath("/mes:facebookid/text()", String.class)) // fill the userid parameter
//                //.to("facebook://user")
//            .end() // end the parallel processing, this is a kind of "join"
//        .end() // stop splitting and start returning
//        .marshal(jaxbMatchResponse); // serialize the java-object from the aggregrator to SOAP/XML

        TwitchCamelAPIWrapper twitch = new TwitchCamelAPIWrapper(this);
        SteamCamelAPIWrapper steam = new SteamCamelAPIWrapper(this);


        if(!isJUnitTest()) {
            from("spring-ws:rootqname:{http://www.han.nl/schemas/messages}MatchRequest?endpointMapping=#matchEndpointMapping")
                    .setExchangePattern(ExchangePattern.InOut)
                    .setHeader(HEADER_KAPPIDO_USER_ID, ns.xpath("/mes:MatchRequest/mes:input/mes:userId/text()", String.class))
                    .split(ns.xpath("/mes:MatchRequest/mes:input/mes:paramList"), new KappidoAggregrate(new FakeD2DProfileManager(), twitch, steam))
                    .parallelProcessing()
                    .setHeader(HEADER_KAPPIDO_WEIGHTING, ns.xpath("/mes:paramList/mes:weighing/text()", String.class))
                    .setHeader(HEADER_KAPPIDO_MATCH_TYPE, ns.xpath("/mes:paramList/mes:matchType/text()", String.class))
//                .choice()
//                    .when(header(HEADER_KAPPIDO_MATCH_TYPE).isEqualTo("gamesStreamed"))
//                        .log("KAAS-gamesStreamed: ${body} | ${headers}")
//                    .when(header(HEADER_KAPPIDO_MATCH_TYPE).isEqualTo("gamesWatched"))
//                        .log("KAAS-gamesWatched: ${body} | ${headers}")
//                    .otherwise()
//                        .log("KAAS-otherwise: GA FIETSEN KUT: ${body}")
//                .end()
                    .end();
        }

        twitch.configure();
        steam.configure();
    }

    public static boolean isJUnitTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }
}
