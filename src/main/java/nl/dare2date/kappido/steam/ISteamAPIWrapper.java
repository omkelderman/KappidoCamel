package nl.dare2date.kappido.steam;

import nl.dare2date.kappido.common.IUserCacheable;

import java.util.List;

/**
 * API-wrapper for the Steam-API
 */
public interface ISteamAPIWrapper extends IUserCacheable<ISteamUser> {
    /**
     * Request a list of steam-games a steam-user owns.
     * <p>
     * The returned list will be lazy-loaded which means that request data from a steam-game-object in that list may
     * result in an other API-request being made.
     *
     * @param steamId The steam-id of the steam-user
     * @return A lazy-loaded list of steam-games
     */
    List<ISteamGame> getOwnedGames(String steamId);

    /**
     * Request user-info of a steam-user
     *
     * @param steamId The steam-id of the steam-user
     * @return A steam-user-object
     */
    ISteamUser getUser(String steamId);

    /**
     * Adds the actual game-data for a given (probably lazy loaded) steam-game object.
     *
     * @param game The steam-game object to use
     */
    void addGameDetails(ISteamGame game);
}
