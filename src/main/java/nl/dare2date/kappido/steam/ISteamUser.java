package nl.dare2date.kappido.steam;

import java.util.List;

/**
 * Representation of a steam-user
 */
public interface ISteamUser {
    /**
     * Get the steam-user-id
     *
     * @return The steam-user-id
     */
    String getSteamId();

    /**
     * Get a lazy-loaded list of steam-games the user owns
     *
     * @return A lazy-loaded list of steam-games
     */
    List<ISteamGame> getOwnedGames();
}
