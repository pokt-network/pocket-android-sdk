package network.pokt.pocketsdk.interfaces;

import network.pokt.pocketsdk.models.Wallet;

public interface WalletRetrieveListener {

    void onRetrieve(Wallet wallet, Exception exception);

}
