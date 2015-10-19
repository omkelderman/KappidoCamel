package nl.dare2date.kappido.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

/**
 * Basic API-wrapper which can handle JSON-endpoints
 */
public abstract class JsonAPIWrapper {
    private IURLResourceProvider urlResourceProvider;

    /**
     * Creates a JsonAPIWrapper with the given {@link IURLResourceProvider} to use for reading data from a URL.
     *
     * @param urlResourceProvider The {@link IURLResourceProvider} to use
     */
    public JsonAPIWrapper(IURLResourceProvider urlResourceProvider) {
        this.urlResourceProvider = urlResourceProvider;
    }

    /**
     * Creates a {@link JsonObject} for a URL-path.
     * <p>
     * Note that for this method to work, the root-json-element on the URL <strong>MUST</strong> be a JSON-Object!
     *
     * @param path A valid URL that will be used to read data from
     * @return A {@link JsonObject} representing the JSON-found on the given URL
     * @throws IOException           If the URL is not valid or not reachable
     * @throws IllegalStateException If the data is not valid JSON
     */
    protected JsonObject getJsonForPath(String path) throws IOException {
        URL url = new URL(path);
        try (BufferedReader reader = urlResourceProvider.getReaderForURL(url)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }
}
