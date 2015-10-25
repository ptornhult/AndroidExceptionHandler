package se.codeunlimited.android.exception.handler;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import se.codeunlimited.android.exception_handler.AbstractExceptionClient;

/**
 * Created by Peter on 20-Oct-15.
 */
public class ExceptionClient extends AbstractExceptionClient {
    private static final String TAG = "ExceptionClient";

    private static final String URL = "[SPECIFY URL ENDPOINT HERE]";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @Override
    public boolean post(String json) {
        try {
            post(URL, json);
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Failed to handle request", e);
            return false;
        }
    }

    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
