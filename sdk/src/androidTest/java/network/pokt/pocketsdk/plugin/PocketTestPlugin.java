package network.pokt.pocketsdk.plugin;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.CreateTransactionException;
import network.pokt.pocketsdk.exceptions.CreateWalletException;
import network.pokt.pocketsdk.exceptions.ImportWalletException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;
import network.pokt.pocketsdk.interfaces.Configuration;
import network.pokt.pocketsdk.interfaces.PocketPlugin;
import network.pokt.pocketsdk.models.Query;
import network.pokt.pocketsdk.models.Transaction;
import network.pokt.pocketsdk.models.Wallet;
import okhttp3.mockwebserver.MockWebServer;

public class PocketTestPlugin extends PocketPlugin {

    final String NETWORK = "TEST";
    final List<String> SUBNETWORKS = new ArrayList<>(Arrays.asList(new String[]{"1", "2", "3"}));

    public PocketTestPlugin(@NotNull Configuration configuration) throws InvalidConfigurationException {
        super(configuration);
    }

    @Override
    public @NotNull Wallet createWallet(@NotNull String subnetwork, Map<String, Object> data) throws CreateWalletException {
        try {
            return new Wallet("0x00000", "12345678", this.getNetwork(), subnetwork, null);
        } catch (JSONException e) {
            throw new CreateWalletException(data, e.getMessage());
        }
    }

    @Override
    public @NotNull Wallet importWallet(@NotNull String privateKey, @NotNull String subnetwork, String address, Map<String, Object> data) throws ImportWalletException {
        try {
            return new Wallet("0x00000", "12345678", this.getNetwork(), subnetwork, null);
        } catch (JSONException e) {
            throw new ImportWalletException(privateKey, address, data, e.getMessage());
        }
    }

    @Override
    public @NotNull Transaction createTransaction(@NotNull Wallet wallet, @NotNull String subnetwork, Map<String, Object> params) throws CreateTransactionException {
        try {
            return new Transaction(this.getNetwork(), subnetwork, "0x0", new JSONObject());
        } catch (JSONException e) {
            throw new CreateTransactionException(wallet, subnetwork, params, e.getMessage());
        }
    }

    @Override
    public @NotNull Query createQuery(@NotNull String subnetwork, Map<String, Object> params, Map<String, Object> decoder) throws CreateQueryException {
        try {
            return new Query(getNetwork(), subnetwork, new JSONObject(), new JSONObject());
        } catch (JSONException e) {
            throw new CreateQueryException(subnetwork, params, decoder, e.getMessage());
        }
    }

    @Override
    public @NotNull String getNetwork() {
        return NETWORK;
    }
}
