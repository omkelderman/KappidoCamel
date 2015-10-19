package nl.dare2date.kappido.steam;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nl.dare2date.kappido.common.IURLResourceProvider;
import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.common.JsonAPIWrapper;
import nl.dare2date.kappido.common.URLResourceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link ISteamAPIWrapper}
 */
public class SteamAPIWrapper extends JsonAPIWrapper implements ISteamAPIWrapper {

    private static final String GET_OWNED_GAMES_PATH = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=%s";
    private static final String GET_GAME_DETAILS = "http://store.steampowered.com/api/appdetails/?appids=%s";
    private static final String STEAM_API_PATH = "SteamAPIKey.txt"; //Within the resources folder.
    private IUserCache<ISteamUser> userCache;
    private final String apiKey;

    /**
     * Creates a {@link SteamAPIWrapper} with the default {@link URLResourceProvider} and the API-key found in the
     * application resources. A file with the name <code>SteamAPIKey.txt</code> must be present in the application
     * resources root containing the api-key to use on the first line.
     *
     * @throws IllegalStateException if the api-key file was not found or was not readable
     */
    public SteamAPIWrapper() {
        this(getDefaultSteamAPIKey());
    }

    /**
     * Creates a {@link SteamAPIWrapper} with the default {@link URLResourceProvider} and given api-key
     *
     * @param apiKey The steam-api-key to use
     */
    public SteamAPIWrapper(String apiKey) {
        this(apiKey, new URLResourceProvider());
    }

    /**
     * Creates a {@link SteamAPIWrapper} with the given {@link URLResourceProvider} and the API-key found in the
     * application resources. A file with the name <code>SteamAPIKey.txt</code> must be present in the application
     * resources root containing the api-key to use on the first line.
     *
     * @param urlResourceProvider The {@link URLResourceProvider} to use while requesting data
     * @throws IllegalStateException if the api-key file was not found or was not readable
     */
    public SteamAPIWrapper(IURLResourceProvider urlResourceProvider) {
        this(getDefaultSteamAPIKey(), urlResourceProvider);
    }

    /**
     * Creates a {@link SteamAPIWrapper} with the given {@link URLResourceProvider} and given api-key.
     *
     * @param apiKey              The steam-api-key to use
     * @param urlResourceProvider The {@link URLResourceProvider} to use while requesting data
     */
    public SteamAPIWrapper(String apiKey, IURLResourceProvider urlResourceProvider) {
        super(urlResourceProvider);
        this.apiKey = apiKey;
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
        try {
            JsonObject root = getJsonForPath(String.format(GET_OWNED_GAMES_PATH, apiKey, steamId));
            JsonArray ownedGamesArray = root.get("response").getAsJsonObject().get("games").getAsJsonArray();
            List<ISteamGame> ownedGames = new ArrayList<>();
            for (JsonElement ownedGameElement : ownedGamesArray) {
                JsonObject ownedGameObject = ownedGameElement.getAsJsonObject();
                ownedGames.add(new SteamGame(ownedGameObject.get("appid").getAsString(), this));//TODO utilize the 'playtime_forever' key.
            }
            return ownedGames;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        try {
            JsonObject root = getJsonForPath(String.format(GET_GAME_DETAILS, game.getId()));
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
