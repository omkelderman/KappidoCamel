package nl.dare2date.kappido.steam;

import java.util.List;

/**
 * Default implementation of {@link ISteamUser}
 */
public class SteamUser implements ISteamUser {
    private final ISteamAPIWrapper steamAPIWrapper;
    private final String steamId;
    private List<ISteamGame> ownedGames;

    /**
     * Creates a new lazy loaded steam-user object with the given steam-user-id and a steamAPIWrapper to use to request
     * the data that's needed for the lazy-loaded parts.
     *
     * @param steamId         The steam-user-id
     * @param steamAPIWrapper A steam-api-wrapper
     */
    SteamUser(String steamId, ISteamAPIWrapper steamAPIWrapper) {
        this.steamId = steamId;
        this.steamAPIWrapper = steamAPIWrapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSteamId() {
        return steamId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ISteamGame> getOwnedGames() {
        if (ownedGames == null) {
            ownedGames = steamAPIWrapper.getOwnedGames(steamId);
        }
        return ownedGames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getSteamId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SteamUser)) return false;
        SteamUser user = (SteamUser) o;
        return steamId.equals(user.steamId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return steamId.hashCode();
    }
}
