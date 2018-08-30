package network.pokt.pocketsdk.exceptions;

import network.pokt.pocketsdk.interfaces.Configuration;

public class InvalidConfigurationException extends Exception {

    private Configuration configuration;

    public InvalidConfigurationException(Configuration configuration, String reason) {
        super(reason);
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
