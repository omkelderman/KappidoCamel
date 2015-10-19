package nl.dare2date.kappido.twitch;

import nl.dare2date.kappido.common.FakeURLResourceProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the twitch-api-wrapper
 */
public class TwitchAPIWrapperTest {

    private TwitchAPIWrapper twitchAPIWrapper;
    private static FakeURLResourceProvider fakeUrlResourceProvider;

    @BeforeClass
    public static void initAll() {
        fakeUrlResourceProvider = new FakeTwitchURLResourceProvider();
    }

    @Before
    public void initialize() {
        twitchAPIWrapper = new TwitchAPIWrapper(fakeUrlResourceProvider);
    }

    @Test
    public void checkUserRetrieval() throws Exception {
        ITwitchUser user = twitchAPIWrapper.getUser("staiain");
        assertEquals(user.getTwitchId(), "staiain");
    }

    @Test
    public void checkUserFollowsOtherUser() throws Exception {
        List<ITwitchUser> users = twitchAPIWrapper.getFollowingUsers("staiain");
        assertTrue(users.contains(new TwitchUser("kommisar", twitchAPIWrapper))); // 0-99|0
        assertTrue(users.contains(new TwitchUser("Woddles", twitchAPIWrapper))); // 0-99|42
        assertTrue(users.contains(new TwitchUser("denkyu", twitchAPIWrapper))); // 100-199|0
        assertTrue(users.contains(new TwitchUser("konnochan", twitchAPIWrapper))); // 100-199|42
        assertTrue(users.contains(new TwitchUser("PortalLifu", twitchAPIWrapper))); // 200-299|0
        assertTrue(users.contains(new TwitchUser("linkmaster500", twitchAPIWrapper))); // 200-229|28
    }
}
