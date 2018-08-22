package network.pokt.pocketsdk.exceptions;

import java.util.Map;

public class CreateQueryException extends Exception {

    String subnetwork;
    Map<String, Object> params;
    java.util.Map<String, Object> decoder;

    public CreateQueryException(String subnetwork, Map<String, Object> params, Map<String, Object> decoder, String reason) {
        super(reason);
        this.subnetwork = subnetwork;
        this.params = params;
        this.decoder = decoder;
    }

    public String getSubnetwork() {
        return subnetwork;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Map<String, Object> getDecoder() {
        return decoder;
    }
}
