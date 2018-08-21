package network.pokt.pocketsdk.models;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class Query {

    protected class Request {
        @NotNull public String network;
        @NotNull public String subnetwork;
        @NotNull public Map<@NotNull String, Object> data;
        @NotNull public Map<@NotNull String, Object> decoder;

        public Request(@NotNull String network, @NotNull String subnetwork, @NotNull Map<@NotNull String, Object> data, @NotNull Map<@NotNull String, Object> decoder) {
            this.network = network;
            this.subnetwork = subnetwork;
            this.data = data;
            this.decoder = decoder;
        }
    }

    protected class Response {
        public Map<@NotNull String, Object> result;
        @NotNull public boolean decoded;
        @NotNull public boolean error;
        public String errorMsg;

        public Response(Map<@NotNull String, Object> result, @NotNull boolean decoded, @NotNull boolean error, String errorMsg) {
            this.result = result;
            this.decoded = decoded;
            this.error = error;
            this.errorMsg = errorMsg;
        }
    }

    @NotNull protected Request request;
    protected Response response;

    public Query(@NotNull String network, @NotNull String subnetwork, @NotNull Map<@NotNull String, Object> data, @NotNull Map<@NotNull String, Object> decoder) {
        this.request = new Request(network, subnetwork, data, decoder);
    }

    public Query(@NotNull String network, @NotNull String subnetwork, @NotNull Map<@NotNull String, Object> data, @NotNull Map<@NotNull String, Object> decoder, Map<@NotNull String, Object> result, @NotNull boolean decoded, @NotNull boolean error, String errorMsg) {
        this.request = new Request(network, subnetwork, data, decoder);
        this.response = new Response(result, decoded, error, errorMsg);
    }

    public boolean isExecuted() {
        return this.response != null;
    }

    public String getNetwork() {
        return this.request.network;
    }

    public String getSubnetwork() {
        return this.request.subnetwork;
    }

    public Map<String, Object> getData() {
        return this.request.data;
    }

    public Map<String, Object> getDecoder() {
        return this.request.decoder;
    }

    public Map<String, Object> getResult() {
        return isExecuted() ? this.response.result : null;
    }

    public boolean isDecoded() {
        return isExecuted() ? this.response.decoded : null;
    }

    public boolean isError() {
        return isExecuted() ? this.response.error : null;
    }

    public String getErrorMsg() {
        return isExecuted() ? this.response.errorMsg : null;
    }
}
