package nl.dare2date.kappido.twitch;

import java.util.List;

/**
 * Representation of a twitch-user
 */
public interface ITwitchUser {
    /**
     * Get the username of the twitch user.
     *
     * @return The twitch username
     */
    String getTwitchId();

    /**
     * Get the name of the game the user has streamed last
     *
     * @return The name of the game
     */
    String getLastPlayedGame();

    /**
     * Get a lazy-loaded list of twitch-users that this user is following.
     *
     * @return A lazy-loaded list of twitch-users
     */
    List<ITwitchUser> getFollowingUsers();
}
