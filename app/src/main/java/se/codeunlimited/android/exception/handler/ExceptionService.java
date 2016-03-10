package se.codeunlimited.android.exception.handler;

import java.util.ArrayList;

import se.codeunlimited.android.exception_handler.AbstractExceptionClient;
import se.codeunlimited.android.exception_handler.AbstractExceptionService;
import se.codeunlimited.android.exception_handler.UnhandledException;

/**
 * Created by Peter on 20-Oct-15.
 */
public class ExceptionService extends AbstractExceptionService {
    private ExceptionClient exceptionClient = new ExceptionClient();
    private ExceptionHandler exceptionHandler = new ExceptionHandler(this);

    @Override
    public AbstractExceptionClient getExceptionClient() {
        return exceptionClient;
    }

    @Override
    public ArrayList<UnhandledException> getExceptions() {
        return exceptionHandler.load();
    }

    @Override
    public void clearExceptions() {
        exceptionHandler.clear(this);
    }
}
