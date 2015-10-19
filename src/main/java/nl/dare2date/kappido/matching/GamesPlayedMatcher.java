package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.steam.ISteamGame;
import nl.dare2date.kappido.steam.ISteamUser;
import nl.dare2date.profile.ID2DProfileManager;

import java.util.*;

/**
 * Finds matches based on the games users are playing.
 * <p>
 * Use case:
 * "Find match by comparing the games played."
 */
public class GamesPlayedMatcher extends SteamMatcher {
    public GamesPlayedMatcher(ID2DProfileManager profileManager, IUserCache<ISteamUser> steamUserCache) {
        super(profileManager, steamUserCache);
    }

    @Override
    protected List<MatchEntry> findMatches(int dare2DateUser, ISteamUser steamUser, Map<Integer, ISteamUser> steamDare2DateUsers) {
        List<MatchEntry> matches = new ArrayList<>();

        //Get a set of the games the user has played (so we can benefit of O(1) when doing a contains()).
        Set<ISteamGame> gamesPlayed = new HashSet<>(steamUser.getOwnedGames());

        //Match by checking for each of the other users if they own the same game.
        for (Map.Entry<Integer, ISteamUser> otherUser : steamDare2DateUsers.entrySet()) {
            if (otherUser.getKey() != dare2DateUser) { //We can't match with ourselves..
                for (ISteamGame otherUserGame : otherUser.getValue().getOwnedGames()) {
                    if (gamesPlayed.contains(otherUserGame)) {
                        MatchEntry entry = new MatchEntry();
                        entry.setUserId(otherUser.getKey());
                        entry.setProbability(1); //Add a matching probability of 1 for every game another user owns too.
                        matches.add(entry);
                    }
                }
            }
        }

        return matches;
    }
}
