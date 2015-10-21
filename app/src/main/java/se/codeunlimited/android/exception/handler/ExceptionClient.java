package se.codeunlimited.android.exception.handler;

import android.os.Build;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import se.codeunlimited.android.exception.handler.R;
import se.codeunlimited.utils.IExceptionClient;
import se.codeunlimited.utils.UnhandledException;

/**
 * Created by Peter on 20-Oct-15.
 */
public class ExceptionClient implements IExceptionClient {
    private static final String TAG = "ExceptionClient";

    private static final String URL = "[URL]";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String getAppName() {
        return App.ctx.getString(R.string.app_name);
    }

    private JSONObject createRequest(final ArrayList<UnhandledException> exceptions) throws JSONException {
        JSONObject request = new JSONObject();

        JSONArray arr = new JSONArray();
        for(UnhandledException ue : exceptions){
            arr.put(ue.toJSON());
        }

        request.put("app_name", getAppName());
        try{ request.put("phone", getDeviceName()); }catch(Exception e){ }

        request.put("exceptions", arr);// Add exceptions here

        return request;
    }

    @Override
    public boolean handle(ArrayList<UnhandledException> exceptions) {
        try {
            JSONObject request = createRequest(exceptions);
            post(URL, request.toString());
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
