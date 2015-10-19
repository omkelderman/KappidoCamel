package nl.dare2date.matchservice;

import nl.dare2date.kappido.matching.*;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.services.MatchResponse;
import nl.dare2date.kappido.services.MatchResult;
import nl.dare2date.kappido.steam.ISteamAPIWrapper;
import nl.dare2date.kappido.steam.SteamUserCache;
import nl.dare2date.kappido.twitch.ITwitchAPIWrapper;
import nl.dare2date.kappido.twitch.TwitchUserCache;
import nl.dare2date.profile.ID2DProfileManager;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.*;

public class KappidoAggregrate implements AggregationStrategy {
    private final Map<String, IMatcher> matchers;
    private Map<Integer, Double> userMatchProbabilities = new HashMap<>();

    public KappidoAggregrate(ID2DProfileManager profileManager, ITwitchAPIWrapper twitchAPIWrapper, ISteamAPIWrapper steamAPIWrapper) {
        TwitchUserCache twitchUserCache = new TwitchUserCache(twitchAPIWrapper);
        twitchAPIWrapper.setCache(twitchUserCache);

        SteamUserCache steamUserCache = new SteamUserCache(steamAPIWrapper);
        steamAPIWrapper.setCache(steamUserCache);

        matchers = new HashMap<>();
        matchers.put("gamesWatched", new GamesWatchedMatcher(profileManager, twitchUserCache));
        matchers.put("mutualFollowings", new MutualFollowingsMatcher(profileManager, twitchUserCache));
        matchers.put("gamesStreamed", new GamesStreamedMatcher(profileManager, twitchUserCache));
        matchers.put("genresPlayed", new GameGenresPlayedMatcher(profileManager, steamUserCache));
        matchers.put("gamesPlayed", new GamesPlayedMatcher(profileManager, steamUserCache));

    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            // first aggregate within the split, reset them crap
            oldExchange = newExchange;
            userMatchProbabilities.clear();
        }

        System.out.print("I AM AGGREGRATING BRO 3!! --> ");
//        System.out.println("Headers: " + newExchange.getIn().getHeaders());
        int dare2dateUserId = newExchange.getIn().getHeader(SocialMediaMatchRoute.HEADER_KAPPIDO_USER_ID, Integer.class);
        String matchType = newExchange.getIn().getHeader(SocialMediaMatchRoute.HEADER_KAPPIDO_MATCH_TYPE, String.class);
        int weighing = newExchange.getIn().getHeader(SocialMediaMatchRoute.HEADER_KAPPIDO_WEIGHTING, Integer.class);

        System.out.printf("%d - %s - %d\n", dare2dateUserId, matchType, weighing);

        IMatcher matcher = matchers.get(matchType);
        if(matcher == null) {
            throw new IllegalStateException("No matcher for match type " + matchType);
        }
        for(MatchEntry match : matcher.findMatches(dare2dateUserId)) {
            Double lastProbability = userMatchProbabilities.get(match.getUserId());

            //When there wasn't a match probability of a stored yet, store the one that was just aqcuired. If there was, store the current one added to the last one.
            userMatchProbabilities.put(match.getUserId(), match.getProbability() * weighing + (lastProbability != null ? lastProbability : 0));
        }

        // Build result
        List<MatchEntry> matches = new ArrayList<>();
        for (Map.Entry<Integer, Double> match : userMatchProbabilities.entrySet()) {
            MatchEntry matchEntry = new MatchEntry();
            matchEntry.setUserId(match.getKey());
            matchEntry.setProbability(match.getValue());
            matches.add(matchEntry);
        }
        matches.sort(new MatchEntryComparator());

        MatchResult matchResult = new MatchResult();
        matchResult.getMatchedUsers().addAll(matches);
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setResult(matchResult);
        oldExchange.getIn().setBody(matchResponse);

        return oldExchange;
    }

    /**
     * Sorting class that sorts based on matching probability. Note that the sort is 'inverted', making the highest
     * probability match be the first element when invoking Collections.sort(list, new MatchEntryComparator()).
     */
    private static class MatchEntryComparator implements Comparator<MatchEntry> {

        @Override
        public int compare(MatchEntry entry1, MatchEntry entry2) {
            return Double.compare(entry2.getProbability(), entry1.getProbability());
        }
    }
}
