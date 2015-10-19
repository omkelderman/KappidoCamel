package nl.dare2date.kappido.twitch;

import java.util.List;

/**
 * Default implementation of {@link ITwitchUser}
 */
public class TwitchUser implements ITwitchUser {
    private final ITwitchAPIWrapper apiWrapper;
    private final String twitchId;
    private String lastPlayedGame;
    private List<ITwitchUser> followingUsers;

    /**
     * Creates a new lazy-loaded twitch user with the given twitch username and api-wrapper to use to request the data
     * that's needed for the lazy-loaded parts.
     *
     * @param twitchId   The twitch username
     * @param apiWrapper A twitch-api-wrapper
     */
    TwitchUser(String twitchId, ITwitchAPIWrapper apiWrapper) {
        this.twitchId = twitchId.toLowerCase();
        this.apiWrapper = apiWrapper;
    }

    /**
     * Creates a new lazy-loaded twitch user with the given twitch username, already known <code>lastPlayedGame</code>
     * property and api-wrapper to use to request the data
     * that's needed for the lazy-loaded parts.
     *
     * @param twitchId       The twitch username
     * @param apiWrapper     A twitch-api-wrapper
     * @param lastPlayedGame The name of the game this user streamed last
     */
    TwitchUser(String twitchId, ITwitchAPIWrapper apiWrapper, String lastPlayedGame) {
        this(twitchId, apiWrapper);
        this.lastPlayedGame = lastPlayedGame;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTwitchId() {
        return twitchId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLastPlayedGame() {
        if (lastPlayedGame == null) {
            return apiWrapper.getUser(twitchId).getLastPlayedGame(); //Retrieve from the API, which will put this new user to the user cache. Therefore this lastPlayedGame doesn't need to be set.
        }
        return lastPlayedGame;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ITwitchUser> getFollowingUsers() {
        if (followingUsers == null) {
            followingUsers = apiWrapper.getFollowingUsers(twitchId);
        }
        return followingUsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getTwitchId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TwitchUser)) return false;
        TwitchUser user = (TwitchUser) o;
        return twitchId.equals(user.twitchId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return twitchId.hashCode();
    }
}
