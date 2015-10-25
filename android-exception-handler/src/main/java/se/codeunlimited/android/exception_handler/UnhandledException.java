package se.codeunlimited.android.exception_handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An object used to store and serialize Exceptions caught in the device
 *
 * Created by Peter Törnhult on 19-Oct-15.
 */
public class UnhandledException {
	private final long timestamp;
	private final String stacktrace;
	private final int appCode;
	private final String packageName;
	private final String device;

	/**
	 * Create from stacktrace
	 * @param appCode the application code
	 * @param packageName the application package name
	 * @param stacktrace the stacktrace we wish to handle
	 * @param device the device name
	 */
	public UnhandledException(int appCode, String packageName, String stacktrace, String device) {
		this.timestamp = System.currentTimeMillis();
		this.stacktrace = stacktrace;
		this.appCode = appCode;
		this.packageName = packageName;
		this.device = device;
	}

	/**
	 * Create from DataInputStream
	 * @param in
	 * @throws IOException
	 */
	public UnhandledException(DataInputStream in) throws IOException {
		this.timestamp = in.readLong();
		this.stacktrace = in.readUTF();
		this.appCode = in.readInt();
		this.packageName = in.readUTF();
		this.device = in.readUTF();
	}

	/**
	 * Write to DataOutputStream
	 * @param out
	 * @throws IOException
	 */
	public void write(DataOutputStream out) throws IOException {
		out.writeLong(timestamp);
		out.writeUTF(stacktrace);
		out.writeInt(appCode);
		out.writeUTF(packageName);
		out.writeUTF(device);
	}

	/**
	 * Serialize to JSON (format must match on serverside as well to handle stacktraces correctly)
	 * @return JSONObject
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("ts", timestamp);
		o.put("trace", stacktrace);
		o.put("app_code", appCode);
		o.put("package_name", packageName);
		o.put("device", device);
		return o;
	}
}
