package nl.dare2date.kappido.twitch;

import nl.dare2date.kappido.common.IUserCacheable;

import java.util.List;

/**
 * API-wrapper for the Twitch-API
 */
public interface ITwitchAPIWrapper extends IUserCacheable<ITwitchUser> {
    /**
     * Request the list of users that the given users is following.
     *
     * @param twitchId The twitch username of which to request the following users
     * @return A list of twitch-users.
     */
    List<ITwitchUser> getFollowingUsers(String twitchId);

    /**
     * Request user-info of a twitch-user
     *
     * @param twitchId The username of the twitch user
     * @return A twitch user object
     */
    ITwitchUser getUser(String twitchId);
}
