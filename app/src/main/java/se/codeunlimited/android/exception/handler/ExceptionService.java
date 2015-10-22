package se.codeunlimited.android.exception.handler;

import android.content.Context;

import java.util.ArrayList;

import se.codeunlimited.android.exception_handler.AbstractExceptionService;
import se.codeunlimited.android.exception_handler.IExceptionClient;
import se.codeunlimited.android.exception_handler.UnhandledException;

/**
 * Created by Peter on 20-Oct-15.
 */
public class ExceptionService extends AbstractExceptionService {
    private ExceptionClient exceptionClient = new ExceptionClient();
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Override
    public IExceptionClient getExceptionClient() {
        return exceptionClient;
    }

    @Override
    public ArrayList<UnhandledException> getExceptions(Context ctx) {
        return exceptionHandler.load(ctx);
    }

    @Override
    public void clearExceptions(Context ctx) {
        exceptionHandler.clear(ctx);
    }
}
