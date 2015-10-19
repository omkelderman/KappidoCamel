package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.twitch.ITwitchUser;
import nl.dare2date.profile.ID2DProfileManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract Twitch based matching class extended by all Twitch-based matching classes.
 */
public abstract class TwitchMatcher implements IMatcher {
    protected ID2DProfileManager profileManager;
    protected IUserCache<ITwitchUser> twitchUserCache;

    public TwitchMatcher(ID2DProfileManager profileManager, IUserCache<ITwitchUser> twitchUserCache) {
        this.profileManager = profileManager;
        this.twitchUserCache = twitchUserCache;
    }

    /**
     * Does things required for all Twitch-based matchers: Retrieves the Twitch account info of the user wanting to match,
     * and gets a list of all Dare2Date users that have a Twitch account.
     *
     * @param dare2DateUser The user that is used to match with.
     * @return A list of Dare2Date user id's and their matching probability with the dare2DateUser. The list can
     * contain duplicates of the same user id's. In the matchmaking algorithm these are combined by adding up the
     * probabilities.
     */
    @Override
    public List<MatchEntry> findMatches(int dare2DateUser) {
        String twitchId = profileManager.getTwitchId(dare2DateUser);
        if (twitchId == null) throw new IllegalStateException("No Twitch Id for user " + dare2DateUser);

        ITwitchUser twitchUser = twitchUserCache.getUserById(twitchId);
        if (twitchUser == null) throw new IllegalStateException("No Twitch user for Twitch Id " + twitchId);

        return findMatches(dare2DateUser, twitchUser, getTwitchDare2DateUsers());
    }

    /**
     * Returns a collection of all Dare2Date users that have a valid Twitch account.
     *
     * @return a map with all Dare2Date users that have a Twitch account, with the key being the Dare2Date user id,
     * and the value the Twitch User details.
     */
    private Map<Integer, ITwitchUser> getTwitchDare2DateUsers() {
        Map<Integer, ITwitchUser> userMap = new HashMap<>();
        for (int dare2DateUser : profileManager.getAllUsers()) {
            String twitchId = profileManager.getTwitchId(dare2DateUser);
            if (twitchId != null) {
                ITwitchUser twitchUser = twitchUserCache.getUserById(twitchId);
                if (twitchUser != null) {
                    userMap.put(dare2DateUser, twitchUser);
                }
            }
        }
        return userMap;
    }

    /**
     * Called when potential matches for a given Dare2Date user are requested. The method should return a list of
     * potential matches and their probability.
     *
     * @param dare2DateUser        a Dare2Date user-id
     * @param twitchUser           Steam user object belonging to the dare2DateUser issuing the match.
     * @param twitchDare2DateUsers Key value map with the keys being dare2date user id's, and their Twitch user object as value.
     * @return A list of Dare2Date user id's and their matching probability with the dare2DateUser. The list can
     * contain duplicates of the same user id's. In the matchmaking algorithm these are combined by adding up the
     * probabilities.
     */
    protected abstract List<MatchEntry> findMatches(int dare2DateUser, ITwitchUser twitchUser, Map<Integer, ITwitchUser> twitchDare2DateUsers);
}
