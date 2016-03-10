package se.codeunlimited.android.exception.handler;

import android.content.Context;
import android.util.Log;

import se.codeunlimited.android.exception_handler.AbstractExceptionHandler;

/**
 * Created by Peter on 21-Oct-15.
 */
public class ExceptionHandler extends AbstractExceptionHandler {
    private static final String TAG = "ExceptionHandler";


    public ExceptionHandler(Context ctx) { super(ctx); }

    public static void setup(Context ctx) {
        try {
            if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof AbstractExceptionHandler)) {
                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(ctx));
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to override DefaultUncaughtExceptionHandler", e);
        }
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
