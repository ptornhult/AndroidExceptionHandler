package se.codeunlimited.android.exception_handler;

import java.util.ArrayList;

/**
 * Created by Peter on 20-Oct-15.
 */
public interface IExceptionClient {
    boolean handle(ArrayList<UnhandledException> exceptions);
}
