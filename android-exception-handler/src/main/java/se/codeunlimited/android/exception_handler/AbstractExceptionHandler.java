package se.codeunlimited.android.exception_handler;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

public abstract class AbstractExceptionHandler implements UncaughtExceptionHandler {
	private static final String TAG = "AExceptionH";

    private UncaughtExceptionHandler defaultUEH;

    public AbstractExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

	/**
	 * Get the application context
	 * @return the application context
	 */
    public abstract Context getContext();

	/**
	 * Specify any file name in which stacktraces should be temporarily saved
	 * @return
	 */
    public abstract String getFileName();

	/**
	 * This will be called as soon as the exception has been logged, you must override this to send the stacktraces to your server
	 */
    public abstract Class<?> getServiceClass();

    @Override
    public void uncaughtException(Thread t, Throwable e) {
    	try{
            log(e);
            
    	}finally{
    		defaultUEH.uncaughtException(t, e);
    	}
    }

    /**
     * Logs the exception and starts the service to send to log-server
     * @param e
     */
    public void log(Throwable e) {
        Log.e(TAG, "Caught an unhandled exception", e);
        String stacktrace = toString(e);

        Log.d(TAG, "Saving trace: " + stacktrace);
        add(getContext(), stacktrace);
        getContext().startService(new Intent(getContext(), getServiceClass()));
    }

	/**
	 * Get a throwable as String stacktrace
	 * @param e
	 * @return stacktrace output
	 */
    private static final String toString(Throwable e){
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        
        return stacktrace;
    }

	/**
	 * Load stacktaces from file
	 * @param ctx
	 * @return
	 */
    public final ArrayList<UnhandledException> load(Context ctx) {
		ArrayList<UnhandledException> list = new ArrayList<>();
		
    	try{
			DataInputStream din = new DataInputStream(ctx.openFileInput(getFileName()));
			
			int count = din.readInt();
			for(int i=count; i>0; i--){
				list.add(new UnhandledException(din));
			}
			
			din.close();
			
		}catch(Exception e){
			Log.e(TAG, "Failed to load saved stacktraces", e);
		}
    	
    	return list;
    }

	/**
	 * Check if there are exceptions stored in the file
	 * Warning: this does a full read of the file
	 * @return
	 */
    public final boolean hasStoredExceptions(){
        return load(getContext()).size()>0;
    }

	/**
	 * Add a new stacktrace and persist to file
	 * @param ctx
	 * @param stacktrace
	 */
    private final void add(Context ctx, String stacktrace) {
        try {
        	ArrayList<UnhandledException> list = load(ctx);
        	list.add(new UnhandledException(getAppCode(ctx), ctx.getPackageName(), stacktrace, getDeviceName()));
        	
        	DataOutputStream dos = new DataOutputStream(ctx.openFileOutput(getFileName(), Context.MODE_PRIVATE));
        	
        	dos.writeInt(list.size());
			for(UnhandledException ue : list){
				ue.write(dos);
			}
			
        	dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * Clear the stacktrace file storage
	 * @param ctx
	 */
    public final void clear(Context ctx) {
        try {
        	DataOutputStream dos = new DataOutputStream(ctx.openFileOutput(getFileName(), Context.MODE_PRIVATE));
        	dos.writeInt(0);
        	dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * Get the app code from a context
	 * @param context
	 * @return the code or -1 if unable to get it
	 */
	public static final int getAppCode(Context context){
        try{
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }catch (Exception e){
            Log.e(TAG, "Couldn't read application version code", e);
        }
        return -1;
    }

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

        if (model==null || manufacturer==null) return "";

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
}