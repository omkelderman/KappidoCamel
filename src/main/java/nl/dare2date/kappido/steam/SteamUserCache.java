package nl.dare2date.kappido.steam;

import nl.dare2date.kappido.common.AbstractUserCache;

/**
 * A cache which contains {@link ISteamUser}s
 */
public class SteamUserCache extends AbstractUserCache<ISteamUser> {
    private ISteamAPIWrapper apiWrapper;

    /**
     * Create a new cache object with the given apiWrapper to use when requesting data that is not in cache
     *
     * @param apiWrapper A steam-api-wrapper
     */
    public SteamUserCache(ISteamAPIWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    /**
     * Create a new steam-user with given id
     *
     * @param id The steam-user-id
     * @return The newly created steam-user
     */
    @Override
    protected ISteamUser createNewUser(String id) {
        return new SteamUser(id, apiWrapper);
    }
}
