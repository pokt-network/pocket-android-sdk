package network.pokt.pocketsdk.interfaces;

import java.util.Map;

import network.pokt.pocketsdk.exceptions.PocketPluginException;
import network.pokt.pocketsdk.models.Wallet;
import network.pokt.pocketsdk.models.Query;
import network.pokt.pocketsdk.models.Transaction;

/**
 * All Pocket Android Plugins must implement this interface.
 */
public abstract class PocketPlugin {

    // Public interface with the Plugin's implementation details
    public abstract Wallet createWallet(Map<String, Object> data) throws PocketPluginException;
    public abstract Wallet importWallet(String privateKey, String address, Map<String, Object> data) throws PocketPluginException;
    public abstract Transaction createTransaction(Wallet wallet, String subnetwork, Map<String, Object> params) throws PocketPluginException;
    public abstract Query createQuery(String subnetwork, Map<String, Object> params, Map<String, Object> decoder) throws PocketPluginException;

    // Class implementation, can be overriden by Plugin
    private Configuration configuration;

    protected PocketPlugin(Configuration configuration) {
        this.configuration = configuration;
    }

}
