package nl.dare2date.kappido.steam;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.common.URLResourceProvider;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Maarten on 20-Oct-15.
 */
public class SteamCamelAPIWrapper extends RouteBuilder implements ISteamAPIWrapper{
    private static final String CAMEL_ENDPOINT = "direct:steam";
    private static final String REQUEST_GAMES = "Games";
    private static final String REQUEST_GAME_DETAILS = "GameDetails";
    private static final String CAMEL_HEADER_ID = "KappidoID";
    private static final String CAMEL_HEADER_REQUEST = "KappidoRequestType";

    private static final String STEAM_API_PATH = "SteamAPIKey.txt"; //Within the resources folder.
    private IUserCache<ISteamUser> userCache;
    private final String getOwnedGamesPath;
    private final String GET_GAME_DETAILS = String.format("http://store.steampowered.com/api/appdetails/?appids=${header.%s}", CAMEL_HEADER_ID);
    private ProducerTemplate template;

    @Override
    public void configure() throws Exception {
        from(CAMEL_ENDPOINT)
        .setExchangePattern(ExchangePattern.InOut)
        .log(String.format("Match type: ${header.%s}, user: ${header.%s}", CAMEL_HEADER_REQUEST, CAMEL_HEADER_ID))
        .choice()
        .when(header(CAMEL_HEADER_REQUEST).contains(REQUEST_GAMES))
        .setHeader(Exchange.HTTP_URI, simple(getOwnedGamesPath))
        .when(header(CAMEL_HEADER_REQUEST).contains(REQUEST_GAME_DETAILS))
        .setHeader(Exchange.HTTP_URI, simple(GET_GAME_DETAILS))
        .end()
        .to("https://ThisIsOverridenBySetHeaderHTTPURI")
        .convertBodyTo(String.class);
    }

    /**
     * Creates a {@link SteamAPIWrapper} with the default {@link URLResourceProvider} and given api-key
     *
     * @param apiKey The steam-api-key to use
     */
    public SteamCamelAPIWrapper(String apiKey) {
        getOwnedGamesPath = String.format("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=${header.%s}", apiKey, CAMEL_HEADER_ID);
    }

    public SteamCamelAPIWrapper() {
        this(getDefaultSteamAPIKey());
    }

    private static String getDefaultSteamAPIKey() {
        InputStream inputStream = SteamAPIWrapper.class.getClassLoader().getResourceAsStream(STEAM_API_PATH);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.readLine();
        } catch (IOException e) {
            throw new IllegalStateException("No Steam API key found in the resources/SteamAPIKey.txt file!", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Setting the cache via a setter as opposed to via the constructor, as due to a circular reference with
     * SteamAPIWrapper and SteamUserCache they can't set each other via the constructor.
     */
    @Override
    public void setCache(IUserCache<ISteamUser> userCache) {
        this.userCache = userCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ISteamGame> getOwnedGames(String steamId) {
        JsonObject root = sendMessage(REQUEST_GAMES, steamId);
        JsonArray ownedGamesArray = root.get("response").getAsJsonObject().get("games").getAsJsonArray();
        List<ISteamGame> ownedGames = new ArrayList<>();
        for (JsonElement ownedGameElement : ownedGamesArray) {
            JsonObject ownedGameObject = ownedGameElement.getAsJsonObject();
            ownedGames.add(new SteamGame(ownedGameObject.get("appid").getAsString(), this));//TODO utilize the 'playtime_forever' key.
        }
        return ownedGames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ISteamUser getUser(String steamId) {
        SteamUser user = new SteamUser(steamId, this);
        if (userCache != null) userCache.addToCache(user, steamId);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addGameDetails(ISteamGame game) {
        JsonObject root = sendMessage(REQUEST_GAME_DETAILS, game.getId());
        JsonObject subRoot = root.get(game.getId()).getAsJsonObject();
        if (subRoot.has("data")) {
            JsonObject gameDetails = subRoot.get("data").getAsJsonObject();
            game.setName(gameDetails.get("name").getAsString());
            List<String> genreIds = new ArrayList<>();
            if (gameDetails.has("genres")) {
                for (JsonElement genreElement : gameDetails.get("genres").getAsJsonArray()) {
                    genreIds.add(genreElement.getAsJsonObject().get("id").getAsString());//TODO maybe use 'id' instead of 'description'?
                }
                game.setGenreIds(genreIds);
            } else {
                game.setGenreIds(Collections.<String>emptyList());
            }
        } else {
            game.setName("<UNKNOWN>");
            game.setGenreIds(Collections.<String>emptyList());
        }
    }

    private JsonObject sendMessage(final String request, final String id) {
        template = getContext().createProducerTemplate();
        Exchange exchange = template.send(CAMEL_ENDPOINT, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(CAMEL_HEADER_REQUEST, request);
                exchange.getIn().setHeader(CAMEL_HEADER_ID, id);
            }
        });
        Object body= exchange.getOut().getBody();
        String stringBody = body.toString();
        return new JsonParser().parse(stringBody).getAsJsonObject();
    }
}
