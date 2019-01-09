package network.pokt.pocketsdk.plugin;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

class PluginMockDispatcher extends Dispatcher {
    @Override
    public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        switch (request.getPath()) {
            case "/transactions":
                return new MockResponse().setResponseCode(200).setBody("{\n" +
                        "          \"network\": \"ETH\",\n" +
                        "          \"subnetwork\": \"4\",\n" +
                        "          \"serialized_tx\": \"0x000\",\n" +
                        "          \"tx_metadata\": {},\n" +
                        "          \"hash\": \"0x000\",\n" +
                        "          \"metadata\": {},\n" +
                        "          \"error\": false,\n" +
                        "          \"error_msg\": null\n" +
                        "        }");
            case "/queries":
                return new MockResponse().setResponseCode(200).setBody("{\n" +
                        "          \"network\": \"ETH\",\n" +
                        "          \"subnetwork\": \"4\",\n" +
                        "          \"query\": {},\n" +
                        "          \"decoder\": {},\n" +
                        "          \"result\": {},\n" +
                        "          \"decoded\": false,\n" +
                        "          \"error\": false,\n" +
                        "          \"error_msg\": null\n" +
                        "        }");
            default:
                return new MockResponse().setResponseCode(404);
        }
    }
}
