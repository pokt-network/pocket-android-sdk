package network.pokt.pocketsdk.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import network.pokt.pocketsdk.interfaces.Codable;

public class Query implements Codable {

    private class Request extends JSONObject {
        @NotNull public String network;
        @NotNull public String subnetwork;
        @NotNull public JSONObject data;
        @NotNull public JSONObject decoder;

        public Request(@NotNull String network, @NotNull String subnetwork, @NotNull JSONObject data, @NotNull JSONObject decoder) throws JSONException {
            super();
            this.network = network;
            this.subnetwork = subnetwork;
            this.data = data;
            this.decoder = decoder;
            this.put("network", network);
            this.put("subnetwork", subnetwork);
            this.put("query", data);
            this.put("decoder", decoder);
        }
    }

    private class Response extends JSONObject {
        public Object result;
        @NotNull public boolean decoded;
        @NotNull public boolean error;
        public String errorMsg;

        public Response(String jsonString) throws JSONException {
            super(jsonString);
            this.result = JSONObject.wrap(this.opt("result"));
            this.decoded = this.getBoolean("decoded");
            this.error = this.getBoolean("error");
            this.errorMsg = this.optString("error_msg");
        }
    }

    private @NotNull Request request;
    private Response response;

    public Query(@NotNull String network, @NotNull String subnetwork, @NotNull JSONObject data, @NotNull JSONObject decoder) throws JSONException {
        this.request = new Request(network, subnetwork, data, decoder);
    }

    public boolean isExecuted() {
        return this.response != null;
    }

    public String getNetwork() {
        return this.request.network;
    }

    @SuppressWarnings("unused")
    public String getSubnetwork() {
        return this.request.subnetwork;
    }

    public JSONObject getData() {
        return this.request.data;
    }

    @SuppressWarnings("unused")
    public JSONObject getDecoder() {
        return this.request.decoder;
    }

    @SuppressWarnings("unused")
    public Object getResult() {
        return isExecuted() ? this.response.result : null;
    }

    @SuppressWarnings("unused")
    public boolean isDecoded() {
        //noinspection ConstantConditions
        return isExecuted() ? this.response.decoded : null;
    }

    @SuppressWarnings("unused")
    public boolean isError() {
        //noinspection ConstantConditions
        return isExecuted() ? this.response.error : null;
    }

    @SuppressWarnings("unused")
    public String getErrorMsg() {
        return isExecuted() ? this.response.errorMsg : null;
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
