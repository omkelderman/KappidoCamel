package nl.dare2date.kappido.twitch;

import nl.dare2date.kappido.common.FakeURLResourceProvider;

/**
 * A {@link FakeURLResourceProvider} that does exactly enough needed for the Unit-Tests to pass
 */
public class FakeTwitchURLResourceProvider extends FakeURLResourceProvider {
    public FakeTwitchURLResourceProvider() {
        super("twitch");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/channels/staiain", "staiain_channel.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/staiain/follows/channels?direction=DESC&limit=100&offset=0&sortby=created_at", "staiain_following_0-99.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/staiain/follows/channels?direction=DESC&limit=100&offset=100&sortby=created_at", "staiain_following_100-199.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/staiain/follows/channels?direction=DESC&limit=100&offset=200&sortby=created_at", "staiain_following_200-229.json");


        registerFakeUrlHandler("https://api.twitch.tv/kraken/channels/omkelderman", "omkelderman_channel.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/omkelderman/follows/channels?direction=DESC&limit=100&offset=0&sortby=created_at", "omkelderman_following_0-48.json");

        registerFakeUrlHandler("https://api.twitch.tv/kraken/channels/minemaarten", "minemaarten_channel.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/minemaarten/follows/channels?direction=DESC&limit=100&offset=0&sortby=created_at", "minemaarten_following_0-8.json");

        registerFakeUrlHandler("https://api.twitch.tv/kraken/channels/quetzi", "quetzi_channel.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/quetzi/follows/channels?direction=DESC&limit=100&offset=0&sortby=created_at", "quetzi_following_0-85.json");

        registerFakeUrlHandler("https://api.twitch.tv/kraken/channels/xikeon", "xikeon_channel.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/xikeon/follows/channels?direction=DESC&limit=100&offset=0&sortby=created_at", "xikeon_following_0-9.json");

        registerFakeUrlHandler("https://api.twitch.tv/kraken/channels/happystick", "happystick_channel.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/happystick/follows/channels?direction=DESC&limit=100&offset=0&sortby=created_at", "happystick_following_0-65.json");

        registerFakeUrlHandler("https://api.twitch.tv/kraken/channels/justin", "justin_channel.json");
        registerFakeUrlHandler("https://api.twitch.tv/kraken/users/justin/follows/channels?direction=DESC&limit=100&offset=0&sortby=created_at", "justin_following_0-32.json");
    }
}
