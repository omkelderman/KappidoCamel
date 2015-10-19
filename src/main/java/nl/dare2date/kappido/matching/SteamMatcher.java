package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.steam.ISteamUser;
import nl.dare2date.profile.ID2DProfileManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract Steam based matching class extended by all Steam-based matching classes.
 */
public abstract class SteamMatcher implements IMatcher {
    protected ID2DProfileManager profileManager;
    protected IUserCache<ISteamUser> steamUserCache;

    public SteamMatcher(ID2DProfileManager profileManager, IUserCache<ISteamUser> steamUserCache) {
        this.profileManager = profileManager;
        this.steamUserCache = steamUserCache;
    }

    /**
     * Does things required for all Steam-based matchers: Retrieves the Steam account info of the user wanting to match,
     * and gets a list of all Dare2Date users that have a Steam account.
     *
     * @param dare2DateUser The user that is used to match with.
     * @return A list of Dare2Date user id's and their matching probability with the dare2DateUser. The list can
     * contain duplicates of the same user id's. In the matchmaking algorithm these are combined by adding up the
     * probabilities.
     */
    @Override
    public List<MatchEntry> findMatches(int dare2DateUser) {
        String steamId = profileManager.getSteamId(dare2DateUser);
        if (steamId == null) throw new IllegalStateException("No Steam Id for user " + dare2DateUser);

        ISteamUser steamUser = steamUserCache.getUserById(steamId);
        if (steamUser == null) throw new IllegalStateException("No Steam user for Steam Id " + steamId);

        return findMatches(dare2DateUser, steamUser, getSteamDare2DateUsers());
    }

    /**
     * Returns a collection of all Dare2Date users that have a valid steam account.
     *
     * @return a map with all Dare2Date users that have a Steam account, with the key being the Dare2Date user id,
     * and the value the Steam User details.
     */
    private Map<Integer, ISteamUser> getSteamDare2DateUsers() {
        Map<Integer, ISteamUser> userMap = new HashMap<>();
        for (int dare2DateUser : profileManager.getAllUsers()) {
            String steamId = profileManager.getSteamId(dare2DateUser);
            if (steamId != null) {
                ISteamUser steamUser = steamUserCache.getUserById(steamId);
                if (steamUser != null) {
                    userMap.put(dare2DateUser, steamUser);
                }
            }
        }
        return userMap;
    }

    /**
     * Called when potential matches for a given Dare2Date user are requested. The method should return a list of
     * potential matches and their probability.
     *
     * @param dare2DateUser       Dare2Date user-id
     * @param steamUser           Steam user object belonging to the dare2DateUser issuing the match.
     * @param steamDare2DateUsers Key value map with the keys being dare2date user id's, and their steam user object as value.
     * @return A list of Dare2Date user id's and their matching probability with the dare2DateUser. The list can
     * contain duplicates of the same user id's. In the matchmaking algorithm these are combined by adding up the
     * probabilities.
     */
    protected abstract List<MatchEntry> findMatches(int dare2DateUser, ISteamUser steamUser, Map<Integer, ISteamUser> steamDare2DateUsers);
}
