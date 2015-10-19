package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.common.FakeURLResourceProvider;
import nl.dare2date.kappido.common.IUserCache;
import nl.dare2date.kappido.services.MatchEntry;
import nl.dare2date.kappido.steam.FakeSteamCache;
import nl.dare2date.kappido.steam.FakeSteamURLResourceProvider;
import nl.dare2date.kappido.steam.ISteamUser;
import nl.dare2date.kappido.steam.SteamAPIWrapper;
import nl.dare2date.profile.FakeD2DProfileManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A test to verify that the {@link GamesPlayedMatcher} works correctly. It uses a fake cache and a fake URL handler
 * to isolate the test.
 */
public class GamePlayedMatcherTest {

    private GamesPlayedMatcher matcher;
    private static FakeURLResourceProvider fakeUrlResourceProvider;

    @BeforeClass
    public static void initAll() {
        fakeUrlResourceProvider = new FakeSteamURLResourceProvider();
    }

    @Before
    public void init() {
        SteamAPIWrapper apiWrapper = new SteamAPIWrapper("steamapikey", fakeUrlResourceProvider);
        IUserCache<ISteamUser> userCache = new FakeSteamCache(apiWrapper);
        apiWrapper.setCache(userCache);
        matcher = new GamesPlayedMatcher(new FakeD2DProfileManager(), userCache);
    }

    /**
     * Check matches of D2D user omkelderman with MineMaarten and Xikeon
     */
    @Test
    public void checkHasMatchForOmkelderman() {
        List<MatchEntry> matches = matcher.findMatches(UserIDs.STEAM_OMKELDERMAN);
        double minemaartenProbability = 0;
        double xikeonProbability = 0;
        for (MatchEntry match : matches) {
            switch (match.getUserId()) {
                case UserIDs.STEAM_MINEMAARTEN:
                    minemaartenProbability += match.getProbability();
                    break;
                case UserIDs.STEAM_XIKEON:
                    xikeonProbability += match.getProbability();
                    break;
            }
        }
        assertEquals(2, minemaartenProbability, 0.001); //2 is the amount of games MineMaarten and omkelderman match on.
        assertEquals(44, xikeonProbability, 0.001); //44 is the amount of games Xikeon and omkelderman match on.
    }

    @Test
    public void checkHasMatchForMineMaarten() {
        List<MatchEntry> matches = matcher.findMatches(UserIDs.STEAM_MINEMAARTEN);
        double quetzProbability = 0;
        double happystickProbability = 0;
        for (MatchEntry match : matches) {
            switch (match.getUserId()) {
                case UserIDs.STEAM_QUETZ:
                    quetzProbability += match.getProbability();
                    break;
                case UserIDs.STEAM_HAPPYSTICK:
                    happystickProbability += match.getProbability();
                    break;
            }
        }
        assertEquals(3, quetzProbability, 0.001); //3 is the amount of games Quetz and MineMaarten match on.
        assertEquals(2, happystickProbability, 0.001); //2 is the amount of games happystick and MineMaarten match on.
    }
}
