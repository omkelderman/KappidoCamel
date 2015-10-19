package nl.dare2date.kappido.steam;

import nl.dare2date.kappido.common.FakeURLResourceProvider;
import nl.dare2date.kappido.common.URLResourceProvider;

import java.io.*;
import java.net.URL;

/**
 * A {@link FakeURLResourceProvider} that does exactly enough needed for the Unit-Tests to pass
 */
public class FakeSteamURLResourceProvider extends FakeURLResourceProvider {
    public FakeSteamURLResourceProvider() {
        super("steam");
        registerFakeUrlHandler("http://store.steampowered.com/api/appdetails/?appids=4000", "app_4000.json");

        registerFakeUrlHandler("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=steamapikey&steamid=76561198034641265", "ownedGames_76561198034641265.json");
        registerFakeUrlHandler("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=steamapikey&steamid=76561198061876520", "ownedGames_76561198061876520.json");
        registerFakeUrlHandler("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=steamapikey&steamid=76561198030627240", "ownedGames_76561198030627240.json");
        registerFakeUrlHandler("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=steamapikey&steamid=76561198042289401", "ownedGames_76561198042289401.json");
        registerFakeUrlHandler("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=steamapikey&steamid=76561197992358589", "ownedGames_76561197992358589.json");
    }

    @Override
    public BufferedReader getReaderForURL(URL url) throws IOException {
        try {
            return super.getReaderForURL(url);
        } catch (IllegalArgumentException e) {
            String path = url.toString();
            if (!path.startsWith("http://store.steampowered.com/api/appdetails/?appids=")) throw e;
            String folderName = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "steam" + File.separatorChar;
            String fileName = "app_" + path.substring(path.indexOf('=') + 1) + ".json";
            File file = new File(folderName + fileName);
            if (!file.exists()) {
                System.out.println(e.getMessage());
                System.out.println("Generating " + fileName + "...");

                URLResourceProvider resourceProvider = new URLResourceProvider();
                File folder = new File(folderName);
                folder.mkdirs();
                try (BufferedReader reader = resourceProvider.getReaderForURL(url)) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        String s;
                        while ((s = reader.readLine()) != null) {
                            writer.write(s);
                        }
                    }
                }
            }
            return new BufferedReader(new FileReader(file));
        }
    }
}
