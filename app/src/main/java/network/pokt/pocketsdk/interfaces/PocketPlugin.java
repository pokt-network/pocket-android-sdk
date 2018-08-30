package network.pokt.pocketsdk.interfaces;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.exceptions.CreateTransactionException;
import network.pokt.pocketsdk.exceptions.CreateWalletException;
import network.pokt.pocketsdk.exceptions.ImportWalletException;
import network.pokt.pocketsdk.exceptions.InvalidConfigurationException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import network.pokt.pocketsdk.models.Wallet;
import network.pokt.pocketsdk.models.Query;
import network.pokt.pocketsdk.models.Transaction;

/**
 * All Pocket Android Plugins must implement this interface.
 */
@SuppressWarnings("unused")
public abstract class PocketPlugin {

    // Public interface with the Plugin's implementation details
    @SuppressWarnings("unused")
    public abstract @NotNull Wallet createWallet(@NotNull String subnetwork, Map<String, Object> data) throws CreateWalletException;

    @SuppressWarnings("unused")
    public abstract @NotNull Wallet importWallet(@NotNull String privateKey, @NotNull String subnetwork, String address, Map<String, Object> data) throws ImportWalletException;

    @SuppressWarnings("unused")
    public abstract @NotNull Transaction createTransaction(@NotNull Wallet wallet, @NotNull String subnetwork, Map<String, Object> params) throws CreateTransactionException;

    @SuppressWarnings("unused")
    public abstract @NotNull Query createQuery(@NotNull String subnetwork, Map<String, Object> params, Map<String, Object> decoder) throws CreateQueryException;

    @SuppressWarnings("unused")
    public abstract @NotNull String getNetwork();

    @SuppressWarnings("unused")
    public abstract @NotNull List<String> getSubnetworks();

    // Class implementation
    private @NotNull Configuration configuration;
    private @NotNull URL queriesURL;
    private @NotNull URL transactionsURL;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @SuppressWarnings("unused")
    public PocketPlugin(@NotNull Configuration configuration) throws InvalidConfigurationException {
        try {
            this.configuration = configuration;
            this.queriesURL = new URL(configuration.getNodeUrl(), "/queries");
            this.transactionsURL = new URL(configuration.getNodeUrl(), "/transactions");
        } catch (MalformedURLException mue) {
            throw new InvalidConfigurationException(this.configuration, mue.getMessage());
        }
    }

     private <R extends Codable> @NotNull R sendRequest(@NotNull R request, @NotNull URL url) throws IOException, JSONException {
        RequestBody body = RequestBody.create(this.JSON, request.toJSONRequestString());
        Request urlRequest = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = this.client.newCall(urlRequest).execute();
        request.fromJSONResponseString(Objects.requireNonNull(response.body()).string());
        return request;
    }

    @SuppressWarnings("unused")
    public <Q extends Query> @NotNull Q executeQuery(@NotNull Q query) throws IOException, JSONException {
        return this.sendRequest(query, queriesURL);
    }

    @SuppressWarnings("unused")
    public <T extends Transaction> @NotNull T sendTransaction(@NotNull T transaction) throws IOException, JSONException {
        return this.sendRequest(transaction, transactionsURL);
    }

    private void sendRequestAsync(@NotNull URL url, @NotNull SendRequestCallback callback) {
        RequestBody body = RequestBody.create(this.JSON, callback.getRequest().toJSONRequestString());
        Request urlRequest = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        this.client.newCall(urlRequest).enqueue(callback);
    }

    @SuppressWarnings("unused")
    public <Q extends Query> void executeQueryAsync(@NotNull SendRequestCallback<Q> callback) {
        this.sendRequestAsync(queriesURL, callback);
    }

    @SuppressWarnings("unused")
    public <T extends Transaction> void sendTransactionAsync(@NotNull SendRequestCallback<T> callback) {
        this.sendRequestAsync(transactionsURL, callback);
    }
}
