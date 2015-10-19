package nl.dare2date.kappido.twitch;

import nl.dare2date.kappido.common.IUserCache;

/**
 * A Twitch User 'cache'. It doesn't cache the values at all, creating a nice stateless object for testing.
 */
public class FakeTwitchCache implements IUserCache<ITwitchUser> {
    private ITwitchAPIWrapper apiWrapper;

    /**
     * Creates the fake cache with the given api-wrapper
     *
     * @param apiWrapper The twitch-api-wrapper needed when creating a user
     */
    public FakeTwitchCache(ITwitchAPIWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    /**
     * {@inheritDoc}
     * Instead of looking up the user in the cache, it'll always create a new user
     */
    @Override
    public ITwitchUser getUserById(String id) {
        return new TwitchUser(id, apiWrapper);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Instead of actually caching anything this method does completely nothing at all
     */
    @Override
    public void addToCache(ITwitchUser twitchUser, String id) {

    }
}
