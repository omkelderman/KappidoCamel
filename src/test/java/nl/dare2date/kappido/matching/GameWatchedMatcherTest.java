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
 * A test to verify that the {@link GamesWatchedMatcher} works correctly. It uses a fake cache and a fake URL handler
 * to isolate the test.
 */
public class GameWatchedMatcherTest {

    private GamesWatchedMatcher matcher;
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
        matcher = new GamesWatchedMatcher(new FakeD2DProfileManager(), userCache);
    }

    @Test
    public void checkHasMatchOmkelderman() {
        List<MatchEntry> matches = matcher.findMatches(UserIDs.TWITCH_OMKELDERMAN);
        double staiainProbability = 0;
        double minemaartenProbability = 0;
        for (MatchEntry match : matches) {
            switch (match.getUserId()) {
                case UserIDs.TWITCH_STAIAIN:
                    staiainProbability += match.getProbability();
                    break;
                case UserIDs.TWITCH_MINEMAARTEN:
                    minemaartenProbability += match.getProbability();
                    break;
            }
        }
        assertEquals(135, staiainProbability, 0.001);   //135 is the amount of steamers Staiain is following that stream a game omkelderman watches.
        assertEquals(7, minemaartenProbability, 0.001); //7 is the amount of steamers MineMaarten is following that stream a game omkelderman watches.
    }

    @Test
    public void checkHasMatchMinemaarten() {
        List<MatchEntry> matches = matcher.findMatches(UserIDs.TWITCH_MINEMAARTEN);
        double quetziProbability = 0;
        double happystickProbability = 0;
        for (MatchEntry match : matches) {
            switch (match.getUserId()) {
                case UserIDs.TWITCH_QUETZI:
                    quetziProbability += match.getProbability();
                    break;
                case UserIDs.TWITCH_HAPPYSTICK:
                    happystickProbability += match.getProbability();
                    break;
            }
        }
        assertEquals(33, quetziProbability, 0.001);     //33 is the amount of steamers Quetzi is following that stream a game MineMaarten watches.
        assertEquals(4, happystickProbability, 0.001);  //4 is the amount of steamers happystick is following that stream a game MineMaarten watches.
    }

}
