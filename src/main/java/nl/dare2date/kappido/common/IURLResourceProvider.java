package nl.dare2date.kappido.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

/**
 * A class implementing this interface provides a {@link BufferedReader} from a {@link URL}.
 */
public interface IURLResourceProvider {
    /**
     * Provides a {@link BufferedReader} which reads from the given URL-endpoint.
     *
     * @param url The {@link URL} object to use for reading
     * @return A {@link BufferedReader} to use for reading data from the given URL-endpoint
     * @throws IOException When the url is invalid or not reachable
     */
    BufferedReader getReaderForURL(URL url) throws IOException;
}
