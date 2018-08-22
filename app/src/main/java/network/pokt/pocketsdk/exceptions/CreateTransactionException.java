package network.pokt.pocketsdk.exceptions;

import java.util.Map;

import network.pokt.pocketsdk.models.Wallet;

public class CreateTransactionException extends Exception {

    Wallet wallet;
    String subnetwork;
    Map<String, Object> params;

    public CreateTransactionException(Wallet wallet, String subnetwork, Map<String, Object> params, String reason) {
        super(reason);
        this.wallet = wallet;
        this.subnetwork = subnetwork;
        this.params = params;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public String getSubnetwork() {
        return subnetwork;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
