package nl.dare2date.kappido.steam;

import nl.dare2date.matchservice.SocialMediaMatchRoute;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.List;

/**
 * Tests the Steam-api-wrapper
 */
public class SteamCamelAPIWrapperTest extends CamelTestSupport {
    private static final String MINEMAARTEN_STEAM_ID = "76561198034641265";
    private static final String GARRYS_MOD_APP_ID = "4000";

    private SteamCamelAPIWrapper steamAPIWrapper;

    @Test
    public void checkUserRetrieval() {
        ISteamUser user = steamAPIWrapper.getUser(MINEMAARTEN_STEAM_ID);
        List<ISteamGame> ownedGames = user.getOwnedGames();
        assertTrue(ownedGames.contains(new SteamGame(GARRYS_MOD_APP_ID, steamAPIWrapper)));
    }

    @Test
    public void checkGameDetails() {
        SteamGame garrysMod = new SteamGame(GARRYS_MOD_APP_ID, steamAPIWrapper);
        assertEquals(garrysMod.getName(), "Garry's Mod");
        assertTrue(garrysMod.getGenreIds().contains("23"));//Genre Id of Simulation
        assertTrue(garrysMod.getGenreIds().contains("28"));//Genre Id of Indie
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        SocialMediaMatchRoute socialMediaMatchRoute = new SocialMediaMatchRoute();
        steamAPIWrapper = new SteamCamelAPIWrapper(socialMediaMatchRoute);
        return socialMediaMatchRoute;
    }
}
