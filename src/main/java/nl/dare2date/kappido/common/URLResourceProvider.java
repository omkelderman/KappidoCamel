package nl.dare2date.kappido.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * The default implementation of {@link IURLResourceProvider} which creates a {@link BufferedReader} from the
 * {@link java.io.InputStream} of the {@link URL} object.
 */
public class URLResourceProvider implements IURLResourceProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedReader getReaderForURL(URL url) throws IOException {
        return new BufferedReader(new InputStreamReader(url.openStream()));
    }
}
