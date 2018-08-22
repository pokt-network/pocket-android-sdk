package network.pokt.pocketsdk.interfaces;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import network.pokt.pocketsdk.exceptions.PocketPluginException;
import network.pokt.pocketsdk.models.Wallet;
import network.pokt.pocketsdk.models.Query;
import network.pokt.pocketsdk.models.Transaction;

/**
 * All Pocket Android Plugins must implement this interface.
 */
public abstract class PocketPlugin {

    // Public interface with the Plugin's implementation details
    @SuppressWarnings("unused")
    public abstract Wallet createWallet(Map<String, Object> data) throws PocketPluginException;

    @SuppressWarnings("unused")
    public abstract Wallet importWallet(String privateKey, String address, Map<String, Object> data) throws PocketPluginException;

    @SuppressWarnings("unused")
    public abstract Transaction createTransaction(Wallet wallet, String subnetwork, Map<String, Object> params) throws PocketPluginException;

    @SuppressWarnings("unused")
    public abstract Query createQuery(String subnetwork, Map<String, Object> params, Map<String, Object> decoder) throws PocketPluginException;

    // Class implementation, can be overriden by Plugin
    @NotNull protected Configuration configuration;
    @NotNull protected URL queriesURL;
    @NotNull protected URL transactionsURL;
    protected final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected OkHttpClient client = new OkHttpClient();

    @SuppressWarnings("unused")
    public PocketPlugin(@NotNull Configuration configuration) throws MalformedURLException {
        this.configuration = configuration;
        this.queriesURL = new URL(configuration.getNodeUrl(), "/queries");
        this.transactionsURL = new URL(configuration.getNodeUrl(), "/transactions");
    }

    protected <R extends Codable> R sendRequest(R request, URL url) throws IOException, JSONException {
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
    public <Q extends Query> Q executeQuery(Q query) throws IOException, JSONException {
        return this.sendRequest(query, queriesURL);
    }

    @SuppressWarnings("unused")
    public <T extends Transaction> T sendTransaction(T transaction) throws IOException, JSONException {
        return this.sendRequest(transaction, transactionsURL);
    }

    protected <R extends Codable> void sendRequestAsync(R request, URL url, SendRequestCallback callback) throws JSONException {
        RequestBody body = RequestBody.create(this.JSON, request.toJSONRequestString());
        Request urlRequest = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        this.client.newCall(urlRequest).enqueue(callback);
    }

    @SuppressWarnings("unused")
    public <Q extends Query> void executeQueryAsync(Q query, SendRequestCallback callback) throws JSONException {
        this.sendRequestAsync(query, queriesURL, callback);
    }

    @SuppressWarnings("unused")
    public <T extends Transaction> void sendTransactionAsync(T transaction, SendRequestCallback callback) throws JSONException {
        this.sendRequestAsync(transaction, transactionsURL, callback);
    }
}
