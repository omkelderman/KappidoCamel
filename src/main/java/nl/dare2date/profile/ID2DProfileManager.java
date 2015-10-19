package nl.dare2date.profile;

import java.util.Set;

/**
 * Dare2Date's profile manager. Used by the application to get required extra information about Dare2Date's users.
 * Would be backed by a database but is simulated with the {@link FakeD2DProfileManager}.
 */
public interface ID2DProfileManager {
    /**
     * Returns a Twitch Id for a certain Dare2Date user id.
     *
     * @param dare2DateUserId A Dare2Date user-id
     * @return The Twitch Id for that Dare2Date user or <code>null</code> if that user does not have a Twitch Id
     */
    String getTwitchId(int dare2DateUserId);

    /**
     * Returns a Steam Id  for a certain Dare2Date user id.
     *
     * @param dare2DateUserId A Dare2Date user-id
     * @return The Steam Id for that Dare2Date user or <code>null</code> if that user does not have a Steam Id
     */
    String getSteamId(int dare2DateUserId);

    /**
     * Returns a set with all Dare2Date user ids.
     *
     * @return A list with Dare2Date user ids
     */
    Set<Integer> getAllUsers();
}
