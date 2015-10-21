package se.codeunlimited.android.exception.handler;

import android.content.Context;
import android.util.Log;

import se.codeunlimited.utils.AbstractExceptionHandler;

/**
 * Created by Peter on 21-Oct-15.
 */
public class ExceptionHandler extends AbstractExceptionHandler {
    private static final String TAG = "ExceptionHandler";

    public ExceptionHandler() { super(); }

    public static void setup() {
        try {
            if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof AbstractExceptionHandler)) {
                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to override DefaultUncaughtExceptionHandler", e);
        }
    }

    @Override
    public Context getContext() {
        return App.ctx;
    }

    @Override
    public String getFileName() {
        return "app.exceptions";
    }

    @Override
    public Class<?> getServiceClass() {
        return ExceptionService.class;
    }
}
