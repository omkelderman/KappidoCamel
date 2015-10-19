package nl.dare2date.kappido.twitch;

import nl.dare2date.kappido.common.AbstractUserCache;

/**
 * A cache which contains {@link ITwitchUser}s
 */
public class TwitchUserCache extends AbstractUserCache<ITwitchUser> {

    private ITwitchAPIWrapper apiWrapper;

    /**
     * Create a new cache object with the given apiWrapper to use when requesting data that is not in cache
     *
     * @param apiWrapper A twitch-api-wrapper
     */
    public TwitchUserCache(ITwitchAPIWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    /**
     * Create a new twitch-user with given username
     *
     * @param id The twitch username
     * @return The newly created twitch-user
     */
    @Override
    protected ITwitchUser createNewUser(String id) {
        return new TwitchUser(id, apiWrapper);
    }
}
