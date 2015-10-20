package nl.dare2date.kappido.twitch;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import nl.dare2date.kappido.common.IUserCache;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maarten on 20-Oct-15.
 */
public class TwitchCamelAPIWrapper extends RouteBuilder implements ITwitchAPIWrapper{
    private static final String CAMEL_ENDPOINT = "direct:twitch";
    private static final String REQUEST_USER = "User";
    private static final String REQUEST_FOLLOWED = "Following";
    private static final String CAMEL_HEADER_USER = "KappidoUser";
    private static final String CAMEL_HEADER_REQUEST = "KappidoRequestType";
    private static final String FOLLOWING_USERS_URL = String.format("https://api.twitch.tv/kraken/users/${header.%s}/follows/channels?direction=DESC&limit=100&offset=%s&sortby=created_at", CAMEL_HEADER_USER, 0);
    private static final String GET_CHANNEL_URL = String.format("https://api.twitch.tv/kraken/channels/${header.%s}",CAMEL_HEADER_USER);
    private ProducerTemplate template;
    private IUserCache<ITwitchUser> userCache;

    @Override
    public void configure() throws Exception {
        from(CAMEL_ENDPOINT)
        .setExchangePattern(ExchangePattern.InOut)
        .log(String.format("Match type: ${header.%s}, user: ${header.%s}", CAMEL_HEADER_REQUEST, CAMEL_HEADER_USER))
        .choice()
        .when(header(CAMEL_HEADER_REQUEST).contains(REQUEST_USER))
        .setHeader(Exchange.HTTP_URI, simple(GET_CHANNEL_URL))
        .when(header(CAMEL_HEADER_REQUEST).contains(REQUEST_FOLLOWED))
        .setHeader(Exchange.HTTP_URI, simple(FOLLOWING_USERS_URL))
        .end()
        .to("https://ThisIsOverridenBySetHeaderHTTPURI")
        .convertBodyTo(String.class);
    }

    public List<ITwitchUser> getFollowingUsers(String twitchId) {
        JsonObject root = sendMessage(REQUEST_FOLLOWED, twitchId);
        List<ITwitchUser> followingUsers = new ArrayList<>();
        JsonArray followingUserArray = root.get("follows").getAsJsonArray();
        for (JsonElement userElement : followingUserArray) {
            JsonObject channel = userElement.getAsJsonObject().get("channel").getAsJsonObject();
            followingUsers.add(getUserForChannelObject(channel));
        }
        return followingUsers;
    }

    public ITwitchUser getUser(String twitchId) {
        JsonObject root = sendMessage(REQUEST_USER, twitchId);
        return getUserForChannelObject(root);
    }

    public void setCache(IUserCache<ITwitchUser> userCache) {
        this.userCache = userCache;
    }

    private ITwitchUser getUserForChannelObject(JsonObject channel) {
        String twitchId = channel.get("name").getAsString();
        JsonElement gameElement = channel.get("game");
        String lastPlayedGame = gameElement.isJsonNull() ? "" : gameElement.getAsString();
        TwitchUser user = new TwitchUser(twitchId, this, lastPlayedGame);
        if (userCache != null) userCache.addToCache(user, twitchId);
        return user;
    }

    private JsonObject sendMessage(final String request, final String twitchId) {
        template = getContext().createProducerTemplate();
        Exchange exchange = template.send(CAMEL_ENDPOINT, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(CAMEL_HEADER_REQUEST, request);
                exchange.getIn().setHeader(CAMEL_HEADER_USER, twitchId);
            }
        });
        Object body= exchange.getOut().getBody();
        String stringBody = body.toString();
        return new JsonParser().parse(stringBody).getAsJsonObject();
    }
}
