package network.pokt.pocketsdk.models;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.pvryan.easycrypt.ECResultListener;
import com.pvryan.easycrypt.symmetric.ECSymmetric;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import network.pokt.pocketsdk.exceptions.WalletPersistenceException;
import network.pokt.pocketsdk.interfaces.WalletRetrieveListener;
import network.pokt.pocketsdk.interfaces.WalletSaveListener;
import network.pokt.pocketsdk.util.StringUtils;

public class Wallet extends JSONObject {

    @NotNull String address;
    @NotNull String privateKey;
    @NotNull String network;
    @NotNull String subnetwork;
    JSONObject data;
    ECSymmetric crypto = new ECSymmetric();

    // Constants
    static final String WALLET_RECORD_KEYS_KEY = "POCKET_WALLETS_RECORD_KEYS";
    static final String ADDRESS_KEY = "address";
    static final String PRIVATE_KEY_KEY = "privateKey";
    static final String NETWORK_KEY = "network";
    static final String SUBNETWORK_KEY = "subnetwork";
    static final String DATA_KEY = "data";

    public Wallet(@NotNull String address, @NotNull String privateKey, @NotNull String network, @NotNull String subnetwork, JSONObject data) throws JSONException {
        super();
        this.address = address;
        this.privateKey = privateKey;
        this.network = network;
        this.subnetwork = subnetwork;
        this.data = data;
        this.put(ADDRESS_KEY, address);
        this.put(PRIVATE_KEY_KEY, privateKey);
        this.put(NETWORK_KEY, network);
        this.put(SUBNETWORK_KEY, subnetwork);
        this.put(DATA_KEY, data);
    }

    public Wallet(String jsonString) throws JSONException {
        super(jsonString);
        this.address = this.getString(ADDRESS_KEY);
        this.privateKey = this.getString(PRIVATE_KEY_KEY);
        this.network = this.getString(NETWORK_KEY);
        this.subnetwork = this.getString(SUBNETWORK_KEY);
        this.data = this.optJSONObject(DATA_KEY);
    }

    @SuppressWarnings("unused")
    public String getAddress() {
        return address;
    }

    @SuppressWarnings("unused")
    public String getPrivateKey() {
        return privateKey;
    }

    public String getNetwork() {
        return network;
    }

    @SuppressWarnings("unused")
    public String getSubnetwork() { return subnetwork; }

    public JSONObject getData() {
        return data;
    }

    public static String recordKey(@NotNull String network, @NotNull String subnetwork, @NotNull String address) {
        return network + "/" + subnetwork + "/" + address;
    }

    public String recordKey() {
        return Wallet.recordKey(this.network, this.subnetwork, this.address);
    }

    // Persistence interfaces
    @SuppressWarnings("unused")
    public static List<String> getWalletsRecordKeys(@NotNull Context context) throws WalletPersistenceException {
        Hawk.init(context).build();
        JSONArray recordKeys = Hawk.get(WALLET_RECORD_KEYS_KEY, new JSONArray());
        List<String> recordKeysList = new ArrayList<>();

        for (int i = 0; i < recordKeys.length(); i++) {
            try {
                recordKeysList.add(recordKeys.getString(i));
            } catch (JSONException e) {
                throw new WalletPersistenceException(e.getMessage());
            }
        }

        return recordKeysList;
    }

    @SuppressWarnings("unused")
    public static boolean isSaved(@NotNull String network, @NotNull String subnetwork, @NotNull String address, @NotNull Context context) {
        Hawk.init(context).build();
        return Hawk.contains(Wallet.recordKey(network, subnetwork, address));
    }

    @SuppressWarnings("unused")
    public boolean isSaved(@NotNull Context context) {
        return Wallet.isSaved(this.network, this.subnetwork, this.address, context);
    }

    @SuppressWarnings("unused")
    public void save(@NotNull String passphrase, @NotNull Context context, @NotNull WalletSaveListener listener) throws WalletPersistenceException {
        if (this.isSaved(context)) {
            throw new WalletPersistenceException("Wallet already exists");
        }

        String recordKey = this.recordKey();

        crypto.encrypt(this.toString(), passphrase, new ECResultListener() {
            @Override
            public void onProgress(int newBytes, long bytesProcessed, long totalBytes) {
                // DO NOTHING
            }

            @Override
            public <T> void onSuccess(T result) {
                String encryptedWalletJSON = (String) result;
                // Save record
                Hawk.init(context).build();
                Hawk.put(recordKey, encryptedWalletJSON);


                // Update recordkey list
                JSONArray recordKeys = Hawk.get(WALLET_RECORD_KEYS_KEY, new JSONArray());
                recordKeys.put(recordKey);
                Hawk.put(WALLET_RECORD_KEYS_KEY, recordKeys);

                // Call listener
                listener.onSave(null);
            }

            @Override
            public void onFailure(@NotNull String message, @NotNull Exception e) {
                listener.onSave(e);
            }
        });
    }

    @SuppressWarnings("unused")
    public static void retrieve(@NotNull String network, @NotNull String subnetwork, @NotNull String address, @NotNull String passphrase, @NotNull Context context, @NotNull WalletRetrieveListener listener) throws WalletPersistenceException {
        if (!Wallet.isSaved(network, subnetwork, address, context)) {
            throw new WalletPersistenceException(String.format("No wallet found for Network %s and Address %s", network, address));
        }

        String recordKey = Wallet.recordKey(network, subnetwork, address);
        Hawk.init(context).build();
        String encryptedWalletJson = Hawk.get(recordKey);

        if (!StringUtils.isValidString(encryptedWalletJson)) {
            throw new WalletPersistenceException("Error retrieving wallet from local storage");
        }

        ECSymmetric crypto = new ECSymmetric();
        crypto.decrypt(encryptedWalletJson, passphrase, new ECResultListener() {
            @Override
            public void onProgress(int newBytes, long bytesProcessed, long totalBytes) {
                // DO NOTHING
            }

            @Override
            public <T> void onSuccess(T result) {
                String decryptedWalletJSON = (String) result;
                try {
                    listener.onRetrieve(new Wallet(decryptedWalletJSON), null);
                } catch (JSONException jsonEx) {
                    listener.onRetrieve(null, jsonEx);
                }
            }

            @Override
            public void onFailure(@NotNull String message, @NotNull Exception e) {
                listener.onRetrieve(null, e);
            }
        });
    }

    @SuppressWarnings("unused")
    public boolean delete(@NotNull Context context) throws WalletPersistenceException {
        if (!this.isSaved(context)) {
            throw new WalletPersistenceException("Wallet has not been saved yet");
        }

        Hawk.init(context).build();
        String recordKey = this.recordKey();

        // Update recordkey list
        JSONArray recordKeys = Hawk.get(WALLET_RECORD_KEYS_KEY, new JSONArray());
        for (int i = 0; i < recordKeys.length(); i++) {
            try {
                if (recordKeys.getString(i).equals(recordKey)) {
                    recordKeys.remove(i);
                    Hawk.put(WALLET_RECORD_KEYS_KEY, recordKeys);
                    break;
                }
            } catch (JSONException e) {
               throw new WalletPersistenceException(e.getMessage());
            }
        }

        // Delete record
        return Hawk.delete(recordKey);
    }
}
