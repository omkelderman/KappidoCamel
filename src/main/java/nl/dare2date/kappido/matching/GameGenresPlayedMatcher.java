package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.steam.ISteamGame;
import nl.dare2date.kappido.steam.ISteamUser;
import nl.dare2date.profile.ID2DProfileManager;

import java.util.*;

/**
 * Finds matches based on the game genres users are playing.
 * <p>
 * Use case:
 * "Find match by comparing the game genres played."
 */
public class GameGenresPlayedMatcher extends SteamMatcher {
    public GameGenresPlayedMatcher(ID2DProfileManager profileManager, IUserCache<ISteamUser> steamUserCache) {
        super(profileManager, steamUserCache);
    }

    @Override
    protected List<MatchEntry> findMatches(int dare2DateUser, ISteamUser steamUser, Map<Integer, ISteamUser> steamDare2DateUsers) {
        List<MatchEntry> matches = new ArrayList<>();

        //Create a set that holds all genre (id's) of games the dare2DateUser owns.
        Set<String> gameGenresPlayed = new HashSet<>();
        for (ISteamGame game : steamUser.getOwnedGames()) {
            for (String genreId : game.getGenreIds()) {
                gameGenresPlayed.add(genreId);
            }
        }

        //Match based on other users their played genres.
        for (Map.Entry<Integer, ISteamUser> otherUser : steamDare2DateUsers.entrySet()) {
            if (otherUser.getKey() != dare2DateUser) { //We can't match with ourselves..
                for (ISteamGame otherUserGame : otherUser.getValue().getOwnedGames()) {
                    for (String otherUserGenre : otherUserGame.getGenreIds()) {
                        if (gameGenresPlayed.contains(otherUserGenre)) {
                            MatchEntry entry = new MatchEntry();
                            entry.setUserId(otherUser.getKey());
                            entry.setProbability(1); //Add a matching probability of 1 for every match another user has with the genres.
                            matches.add(entry);
                        }
                    }
                }
            }
        }

        return matches;
    }
}
