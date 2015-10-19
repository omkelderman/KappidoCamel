package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.twitch.ITwitchUser;
import nl.dare2date.profile.ID2DProfileManager;

import java.util.*;

/**
 * Finds matches based on the streamers users are following.
 * <p>
 * Use case:
 * "Find match by comparing the mutual followers."
 */
public class MutualFollowingsMatcher extends TwitchMatcher {
    public MutualFollowingsMatcher(ID2DProfileManager profileManager, IUserCache<ITwitchUser> twitchUserCache) {
        super(profileManager, twitchUserCache);
    }

    @Override
    protected List<MatchEntry> findMatches(int dare2DateUser, ITwitchUser twitchUser, Map<Integer, ITwitchUser> twitchDare2DateUsers) {
        List<MatchEntry> matches = new ArrayList<>();

        //Get a set of the users the user is following (so we can benefit of O(1) when doing a contains()).
        Set<ITwitchUser> followingUsers = new HashSet<>(twitchUser.getFollowingUsers());

        //Match based on mutual followings
        for (Map.Entry<Integer, ITwitchUser> otherTwitchUser : twitchDare2DateUsers.entrySet()) {
            if (otherTwitchUser.getKey() != dare2DateUser) { //We can't match with ourselves..
                for (ITwitchUser user : otherTwitchUser.getValue().getFollowingUsers()) {
                    if (followingUsers.contains(user)) {
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
