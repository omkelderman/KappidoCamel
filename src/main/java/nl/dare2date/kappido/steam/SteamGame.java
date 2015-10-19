package nl.dare2date.kappido.steam;

import java.util.List;

/**
 * Default implementation of {@link ISteamGame}
 */
public class SteamGame implements ISteamGame {
    private final ISteamAPIWrapper steamAPIWrapper;
    private final String appId;
    private String name;
    private List<String> genres;

    /**
     * Creates a new lazy loaded steam-game object with the given app-id and a steamAPIWrapper to use to request the
     * data that's needed for the lazy-loaded parts.
     *
     * @param appId           The steam-game app-id
     * @param steamAPIWrapper A steam-api-wrapper
     */
    public SteamGame(String appId, ISteamAPIWrapper steamAPIWrapper) {
        this.steamAPIWrapper = steamAPIWrapper;
        this.appId = appId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return appId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        if (name == null) steamAPIWrapper.addGameDetails(this);
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGenreIds(List<String> genres) {
        this.genres = genres;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getGenreIds() {
        if (genres == null) steamAPIWrapper.addGameDetails(this);
        return genres;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SteamGame)) return false;
        SteamGame steamGame = (SteamGame) o;
        return appId.equals(steamGame.appId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return appId.hashCode();
    }
}
