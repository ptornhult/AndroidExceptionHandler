package se.codeunlimited.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Peter on 20-Oct-15.
 */
public class App extends Application {
    public static Context ctx;
    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();

        ExceptionHandler.setup();
    }
}
