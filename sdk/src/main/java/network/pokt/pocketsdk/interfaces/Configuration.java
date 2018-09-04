package network.pokt.pocketsdk.interfaces;

import java.net.MalformedURLException;
import java.net.URL;

public interface Configuration {

    /**
     * Returns the given Node URL for this configuration
     * @return URL
     */
    URL getNodeUrl() throws MalformedURLException;

}
