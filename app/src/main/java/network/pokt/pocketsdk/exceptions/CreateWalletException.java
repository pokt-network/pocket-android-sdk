package network.pokt.pocketsdk.exceptions;

import java.util.Map;

public class CreateWalletException extends Exception {

    Map<String, Object> data;

    public CreateWalletException(Map<String, Object> data, String reason) {
        super(reason);
        this.data = data;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
