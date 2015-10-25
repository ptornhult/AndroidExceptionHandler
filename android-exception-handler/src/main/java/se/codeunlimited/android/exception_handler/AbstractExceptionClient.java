package se.codeunlimited.android.exception_handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Peter on 20-Oct-15.
 */
public abstract class AbstractExceptionClient {

    public boolean handle(ArrayList<UnhandledException> exceptions) throws JSONException {
        JSONObject request = new JSONObject();

        JSONArray arr = new JSONArray();
        for(UnhandledException ue : exceptions){
            arr.put(ue.toJSON());
        }

        request.put("exceptions", arr);

        return post(request.toString());
    }

    public abstract boolean post(String json);
}
