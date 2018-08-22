package network.pokt.pocketsdk.interfaces;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class SendRequestCallback<R extends Codable> implements Callback {

    @NotNull protected R request;

    public SendRequestCallback(@NotNull R request) {
        this.request = request;
    }

    public abstract void onResponse(R response, Exception exception);

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        onResponse(null, e);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        try {
            ResponseBody body = response.body();
            this.request.fromJSONResponseString(Objects.requireNonNull(body).string());
            onResponse(this.request, null);
        } catch (Exception exception) {
            onResponse(null, exception);
        }
    }
}
