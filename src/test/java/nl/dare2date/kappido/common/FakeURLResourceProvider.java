package nl.dare2date.kappido.common;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A fake {@link IURLResourceProvider} to use for testing API-wrappers. It will load the content of a resource file
 * instead of connecting to an actual URL
 */
public abstract class FakeURLResourceProvider implements IURLResourceProvider {
    private final String resourceFolder;
    private final Map<String, String> fakeUrlHandlers;

    /**
     * Creates a new fake URL-resource provider
     *
     * @param resourceFolder The name of the folder within the resources folder in which the files are stored used by
     *                       this {@link FakeURLResourceProvider}
     */
    public FakeURLResourceProvider(String resourceFolder) {
        this.resourceFolder = resourceFolder;
        this.fakeUrlHandlers = new HashMap<>();
    }

    /**
     * Link a url to a file
     *
     * @param url      The url that should have been used
     * @param filename The name of the file that will be served instead
     */
    public void registerFakeUrlHandler(String url, String filename) {
        String path = resourceFolder + File.separatorChar + filename;
        fakeUrlHandlers.put(url, path);
    }

    /**
     * {@inheritDoc}
     * Instead of connecting to the url, it will connect to the file linked with that url
     *
     * @throws IllegalArgumentException If the url has not been linked to a file
     */
    @Override
    public BufferedReader getReaderForURL(URL url) throws IOException {
        String resourcePath = fakeUrlHandlers.get(url.toString());
        if (resourcePath == null) {
            throw new IllegalArgumentException("No fake handler available for url: " + url);
        } else {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(resourcePath);
            if (inputStream == null) throw new IllegalStateException("No file found at " + resourcePath);
            return new BufferedReader(new InputStreamReader(inputStream));
        }
    }
}
