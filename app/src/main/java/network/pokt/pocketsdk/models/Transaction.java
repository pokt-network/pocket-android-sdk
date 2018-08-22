package network.pokt.pocketsdk.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import network.pokt.pocketsdk.interfaces.Codable;

public class Transaction implements Codable {

    protected class Request extends JSONObject {

        @NotNull public String network;
        @NotNull public String subnetwork;
        @NotNull public String serializedTransaction;
        @NotNull public JSONObject transactionMetadata;

        public Request(@NotNull String network, @NotNull String subnetwork, @NotNull String serializedTransaction, @NotNull JSONObject transactionMetadata) throws JSONException {
            super();
            this.network = network;
            this.subnetwork = subnetwork;
            this.serializedTransaction = serializedTransaction;
            this.transactionMetadata = transactionMetadata;
            this.put("network", network);
            this.put("subnetwork", subnetwork);
            this.put("serialized_tx", serializedTransaction);
            this.put("tx_metadata", transactionMetadata);
        }
    }

    protected class Response extends JSONObject {
        public String hash;
        public JSONObject metadata;
        public boolean error;
        public String errorMsg;

        public Response(String jsonString) throws JSONException {
            super(jsonString);
            this.hash = this.optString("hash");
            this.metadata = this.optJSONObject("metadata");
            this.error = this.getBoolean("error");
            this.errorMsg = this.optString("error_msg");
        }
    }

    private Request request;
    private Response response;

    public Transaction(@NotNull String network, @NotNull String subnetwork, @NotNull String serializedTransaction, @NotNull JSONObject transactionMetadata) throws JSONException {
        this.request = new Request(network, subnetwork, serializedTransaction, transactionMetadata);
    }

    public boolean isSent() {
        return this.response != null;
    }

    public String getNetwork() {
        return this.request.network;
    }

    @SuppressWarnings("unused")
    public String getSubnetwork() {
        return this.request.subnetwork;
    }

    @SuppressWarnings("unused")
    public String getSerializedTransaction() {
        return this.request.serializedTransaction;
    }

    @SuppressWarnings("unused")
    public JSONObject getTransactionMetadata() {
        return this.request.transactionMetadata;
    }

    @SuppressWarnings("unused")
    public String getHash() {
        return isSent() ? this.response.hash : null;
    }

    @SuppressWarnings("unused")
    public  JSONObject getMetadata() {
        return isSent() ? this.response.metadata : null;
    }

    @SuppressWarnings("unused")
    public boolean isError() {
        //noinspection ConstantConditions
        return isSent() ? this.response.error : null;
    }

    @SuppressWarnings("unused")
    public String getErrorMsg() {
        return isSent() ? this.response.errorMsg : null;
    }

    @Override
    public String toJSONRequestString() {
        return this.request.toString();
    }

    @Override
    public void fromJSONResponseString(String json) throws JSONException {
        this.response = new Response(json);
    }

}
