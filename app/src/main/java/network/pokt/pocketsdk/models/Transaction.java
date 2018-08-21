package network.pokt.pocketsdk.models;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Transaction {

    protected class Request {

        @NotNull public String network;
        @NotNull public String subnetwork;
        @NotNull public String serializedTransaction;
        @NotNull public Map<@NotNull String, Object> transactionMetadata;

        public Request(@NotNull String network, @NotNull String subnetwork, @NotNull String serializedTransaction, Map<@NotNull String, Object> transactionMetadata) {
            this.network = network;
            this.subnetwork = subnetwork;
            this.serializedTransaction = serializedTransaction;
            this.transactionMetadata = transactionMetadata;
        }
    }

    protected class Response {
        public String hash;
        @NotNull public Map<@NotNull String, Object> metadata;
        @NotNull public boolean error;
        public String errorMsg;

        public Response(String hash, @NotNull Map<@NotNull String, Object> metadata, @NotNull boolean error, String errorMsg) {
            this.hash = hash;
            this.metadata = metadata;
            this.error = error;
            this.errorMsg = errorMsg;
        }
    }

    private Request request;
    private Response response;

    public Transaction(@NotNull String network, @NotNull String subnetwork, @NotNull String serializedTransaction, @NotNull Map<@NotNull String, Object> transactionMetadata) {
        this.request = new Request(network, subnetwork, serializedTransaction, transactionMetadata);
    }

    public Transaction(@NotNull String network, @NotNull String subnetwork, @NotNull String serializedTransaction, @NotNull Map<@NotNull String, Object> transactionMetadata, String hash, @NotNull Map<@NotNull String, Object> metadata, @NotNull boolean error, String errorMsg) {
        this.request = new Request(network, subnetwork, serializedTransaction, transactionMetadata);
        this.response = new Response(hash, metadata, error, errorMsg);
    }

    public boolean isSent() {
        return this.response != null;
    }

    public String getNetwork() {
        return this.request.network;
    }

    public String getSubnetwork() {
        return this.request.subnetwork;
    }

    public String getSerializedTransaction() {
        return this.request.serializedTransaction;
    }

    public Map<@NotNull String, Object> getTransactionMetadata() {
        return this.request.transactionMetadata;
    }

    public String getHash() {
        return isSent() ? this.response.hash : null;
    }

    public  Map<@NotNull String, Object> getMetadata() {
        return isSent() ? this.response.metadata : null;
    }

    public boolean isError() {
        return isSent() ? this.response.error : null;
    }

    public String getErrorMsg() {
        return isSent() ? this.response.errorMsg : null;
    }

}
