package nl.dare2date.kappido.steam;

import nl.dare2date.kappido.common.FakeURLResourceProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Steam-api-wrapper
 */
public class SteamAPIWrapperTest {
    private static final String MINEMAARTEN_STEAM_ID = "76561198034641265";
    private static final String GARRYS_MOD_APP_ID = "4000";

    private SteamAPIWrapper steamAPIWrapper;
    private static FakeURLResourceProvider fakeUrlResourceProvider;

    @BeforeClass
    public static void initAll() {
        fakeUrlResourceProvider = new FakeSteamURLResourceProvider();
    }

    @Before
    public void init() {
        steamAPIWrapper = new SteamAPIWrapper("steamapikey", fakeUrlResourceProvider);
    }

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
}
