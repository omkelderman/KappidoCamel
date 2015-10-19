package nl.dare2date.kappido.steam;

import nl.dare2date.kappido.common.IUserCache;

/**
 * A fake cache that doesn't actually cache anything.
 */
public class FakeSteamCache implements IUserCache<ISteamUser> {
    private final ISteamAPIWrapper apiWrapper;

    /**
     * Creates the fake cache with the given api-wrapper
     *
     * @param apiWrapper The steam-api-wrapper needed when creating a user
     */
    public FakeSteamCache(ISteamAPIWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    /**
     * {@inheritDoc}
     * Instead of looking up the user in the cache, it'll always create a new user
     */
    @Override
    public ISteamUser getUserById(String id) {
        return new SteamUser(id, apiWrapper);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Instead of actually caching anything this method does completely nothing at all
     */
    @Override
    public void addToCache(ISteamUser steamUser, String id) {
    }
}
