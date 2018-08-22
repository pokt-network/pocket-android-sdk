package network.pokt.pocketsdk.exceptions;

import java.util.Map;

public class ImportWalletException extends Exception {

    String privateKey;
    String address;
    Map<String, Object> data;

    public ImportWalletException(String privateKey, String address, Map<String, Object> data, String reason) {
        super(reason);
        this.privateKey = privateKey;
        this.address = address;
        this.data = data;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getAddress() {
        return address;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
