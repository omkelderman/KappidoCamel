package nl.dare2date.kappido.steam;

import java.util.List;

/**
 * Representation of a steam-game
 */
public interface ISteamGame {
    /**
     * Get the steam-game-id of the game
     *
     * @return The game-id
     */
    String getId();

    /**
     * Sets the game name
     *
     * @param name The game name
     */
    void setName(String name);

    /**
     * Get the game-name
     *
     * @return The game name
     */
    String getName();

    /**
     * Sets the list of genre-ids
     *
     * @param genres A list of genre-ids
     */
    void setGenreIds(List<String> genres);

    /**
     * Sets the list of genre-ids
     *
     * @return A list of genre-ids
     */
    List<String> getGenreIds();
}
