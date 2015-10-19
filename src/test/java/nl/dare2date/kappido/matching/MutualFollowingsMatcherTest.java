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
 * A test to verify that the {@link MutualFollowingsMatcher} works correctly. It uses a fake cache and a fake URL handler
 * to isolate the test.
 */
public class MutualFollowingsMatcherTest {

    private MutualFollowingsMatcher matcher;
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
        matcher = new MutualFollowingsMatcher(new FakeD2DProfileManager(), userCache);
    }

    @Test
    public void checkHasMatchOmkelderman() {
        List<MatchEntry> matches = matcher.findMatches(UserIDs.TWITCH_OMKELDERMAN);
        double staiainProbability = 0;
        double justinProbability = 0;
        for (MatchEntry match : matches) {
            switch (match.getUserId()) {
                case UserIDs.TWITCH_STAIAIN:
                    staiainProbability += match.getProbability();
                    break;
                case UserIDs.TWITCH_JUSTIN:
                    justinProbability += match.getProbability();
                    break;
            }
        }
        assertEquals(11, staiainProbability, 0.001); //Staiain has 11 shared followings with omkelderman.
        assertEquals(0, justinProbability, 0.001); //Justin has 0 shared followings with omkelderman.
    }

    @Test
    public void checkHasMatchMinemaarten() {
        List<MatchEntry> matches = matcher.findMatches(UserIDs.TWITCH_MINEMAARTEN);
        double omkeldermanProbability = 0;
        double quetziProbability = 0;
        for (MatchEntry match : matches) {
            switch (match.getUserId()) {
                case UserIDs.TWITCH_OMKELDERMAN:
                    omkeldermanProbability += match.getProbability();
                    break;
                case UserIDs.TWITCH_QUETZI:
                    quetziProbability += match.getProbability();
                    break;
            }
        }
        assertEquals(0, omkeldermanProbability, 0.001); //omkelderman has 0 shared followings with minemaarten.
        assertEquals(5, quetziProbability, 0.001); //quetzi has 5 shared followings with minemaarten.
    }
}
