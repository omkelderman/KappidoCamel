package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.common.FakeURLResourceProvider;
import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.twitch.FakeTwitchCache;
import nl.dare2date.kappido.twitch.FakeTwitchURLResourceProvider;
import nl.dare2date.kappido.twitch.ITwitchUser;
import nl.dare2date.kappido.twitch.TwitchAPIWrapper;
import nl.dare2date.profile.FakeD2DProfileManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A test to verify that the {@link GamesStreamedMatcher} works correctly. It uses a fake cache and a fake URL handler
 * to isolate the test.
 */
public class GameStreamedMatcherTest {

    private GamesStreamedMatcher matcher;
    private static FakeURLResourceProvider fakeUrlResourceProvider;

    @BeforeClass
    public static void initAll() {
        fakeUrlResourceProvider = new FakeTwitchURLResourceProvider();
    }

    @Before
    public void init() {
        TwitchAPIWrapper apiWrapper = new TwitchAPIWrapper(fakeUrlResourceProvider);
        IUserCache<ITwitchUser> userCache = new FakeTwitchCache(apiWrapper);
        apiWrapper.setCache(userCache);
        matcher = new GamesStreamedMatcher(new FakeD2DProfileManager(), userCache);
    }

    @Test
    public void checkHasMatchForOmkelderman() {
        List<MatchEntry> matches = matcher.findMatches(UserIDs.TWITCH_OMKELDERMAN);

        double minemaartenProbability = 0;
        double xikeonProbability = 0;

        for (MatchEntry match : matches) {
            switch (match.getUserId()) {
                case UserIDs.TWITCH_MINEMAARTEN:
                    minemaartenProbability += match.getProbability();
                    break;
                case UserIDs.TWITCH_XIKEON:
                    xikeonProbability += match.getProbability();
                    break;
            }
        }

        assertEquals(0, minemaartenProbability, 0.001); // omkelderman and minemaarten don't have the same "last streamed game"
        assertEquals(1, xikeonProbability, 0.001); // omkelderman and xikeon have the same "last streamed game"
    }

    @Test
    public void checkHasMatchForMineMaarten() {
        List<MatchEntry> matches = matcher.findMatches(UserIDs.TWITCH_MINEMAARTEN);

        double quetziProbability = 0;
        double omkeldermanProbability = 0;

        for (MatchEntry match : matches) {
            switch (match.getUserId()) {
                case UserIDs.TWITCH_QUETZI:
                    quetziProbability += match.getProbability();
                    break;
                case UserIDs.TWITCH_OMKELDERMAN:
                    omkeldermanProbability += match.getProbability();
                    break;
            }
        }

        assertEquals(1, quetziProbability, 0.001); // minemaarten and quetzi have the same "last streamed game"
        assertEquals(0, omkeldermanProbability, 0.001); // minemaarten and omkelderman don't have the same "last streamed game"
    }
}
