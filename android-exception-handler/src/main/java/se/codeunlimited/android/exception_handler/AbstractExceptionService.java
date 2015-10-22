package se.codeunlimited.android.exception_handler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

/**
 * Abstract service template for sending exceptions to a service backend
 */
public abstract class AbstractExceptionService extends Service {
	private static final String TAG = "ExceptionService";

	/**
	 * Get an instance of the Exception API
	 * (Used to load
	 * @return
	 */
	public abstract IExceptionClient getExceptionClient();

    /**
     * Get all exceptions
     * @param ctx
     * @return
     */
	public abstract ArrayList<UnhandledException> getExceptions(Context ctx);

    /**
     * Clear all exceptions
     * @param ctx
     */
	public abstract void clearExceptions(Context ctx);

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, "onBind()");
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand(intent, flags, startId)");

        handleExceptionsAsynch();

        return Service.START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}

    private void handleExceptionsAsynch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handleExceptions();
            }
        }).start();
    }

    private void handleExceptions() {
        ArrayList<UnhandledException> exceptions = getExceptions(this);

        if (exceptions.size() > 0) {
            getExceptionClient().handle(exceptions);
            clearExceptions(this);
        }

        stopSelf();
}
}
