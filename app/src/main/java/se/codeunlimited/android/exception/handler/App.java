package se.codeunlimited.android.exception.handler;

import android.app.Application;

/**
 * Created by Peter on 20-Oct-15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ExceptionHandler.setup(getApplicationContext());
    }
}
