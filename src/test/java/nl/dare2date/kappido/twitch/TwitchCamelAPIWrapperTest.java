package nl.dare2date.kappido.twitch;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.List;

/**
 * Tests the twitch-api-wrapper
 */
public class TwitchCamelAPIWrapperTest extends CamelTestSupport {

    private TwitchCamelAPIWrapper twitchAPIWrapper;

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
      /*  assertTrue(users.contains(new TwitchUser("denkyu", twitchAPIWrapper))); // 100-199|0
        assertTrue(users.contains(new TwitchUser("konnochan", twitchAPIWrapper))); // 100-199|42
        assertTrue(users.contains(new TwitchUser("PortalLifu", twitchAPIWrapper))); // 200-299|0
        assertTrue(users.contains(new TwitchUser("linkmaster500", twitchAPIWrapper))); // 200-229|28*/
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return twitchAPIWrapper = new TwitchCamelAPIWrapper();
    }
}
