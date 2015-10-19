package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.twitch.ITwitchUser;
import nl.dare2date.profile.ID2DProfileManager;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Finds matches based on the games users are watching on Twitch.
 * <p>
 * Use case:
 * "Find match by comparing the game genres watched."
 */
public class GamesWatchedMatcher extends TwitchMatcher {

    public GamesWatchedMatcher(ID2DProfileManager profileManager, IUserCache<ITwitchUser> twitchUserCache) {
        super(profileManager, twitchUserCache);
    }

    @Override
    public List<MatchEntry> findMatches(int dare2DateUser, ITwitchUser twitchUser, Map<Integer, ITwitchUser> twitchDare2DateUsers) {
        List<MatchEntry> matches = new ArrayList<>();

        List<ITwitchUser> followingUsers = twitchUser.getFollowingUsers();

        Set<String> watchedGames = new HashSet<>(); //Fill an HashSet with the watched games, so we can utilize a O(1) lookup speed later.
        for (ITwitchUser followingUser : followingUsers) {
            String watchedGame = followingUser.getLastPlayedGame();
            if (!StringUtils.isEmpty(watchedGame)) watchedGames.add(watchedGame);
        }

        for (Map.Entry<Integer, ITwitchUser> otherTwitchUser : twitchDare2DateUsers.entrySet()) {
            if (otherTwitchUser.getKey() != dare2DateUser) { //We can't match with ourselves..
                List<ITwitchUser> otherFollowingUsers = otherTwitchUser.getValue().getFollowingUsers();
                for (ITwitchUser otherFollowingUser : otherFollowingUsers) {
                    if (watchedGames.contains(otherFollowingUser.getLastPlayedGame())) {
                        MatchEntry entry = new MatchEntry();
                        entry.setUserId(otherTwitchUser.getKey());
                        entry.setProbability(1);
                        matches.add(entry);
                    }
                }
            }
        }

        return matches;
    }
}
