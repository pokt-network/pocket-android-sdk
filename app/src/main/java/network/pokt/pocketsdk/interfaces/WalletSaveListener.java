package network.pokt.pocketsdk.interfaces;

public interface WalletSaveListener {

    // Gets called when wallet is successfully encrypted and persisted to the local device
    void onSave(Exception e);
}
