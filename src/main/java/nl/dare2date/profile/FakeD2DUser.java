package nl.dare2date.profile;

/**
 * Class that describes a Dare2Date user. Is instantiated only by GSON.
 */
public class FakeD2DUser {
    private int userId;
    private String twitchId;
    private String steamId;

    public int getUserId() {
        return userId;
    }

    public String getSteamId() {
        return steamId;
    }

    public String getTwitchId() {
        return twitchId;
    }
}
