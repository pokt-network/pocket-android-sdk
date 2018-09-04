package network.pokt.pocketsdk.interfaces;

import org.json.JSONException;

public interface Codable {

    String toJSONRequestString();
    void fromJSONResponseString(String json) throws JSONException;

}
