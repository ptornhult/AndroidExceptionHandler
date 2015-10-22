package se.codeunlimited.android.exception_handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An object used to store and serialize timestamp, stacktrace and appCode
 *
 * Created by Peter Törnhult on 19-Oct-15.
 */
public class UnhandledException {
	private final long timestamp;
	private final String stacktrace;
	private final int appCode;

	/**
	 * Create from stacktrace
	 * @param appCode the application code
	 * @param stacktrace the stacktrace we wish to handle
	 */
	public UnhandledException(int appCode, String stacktrace) {
		this.timestamp = System.currentTimeMillis();
		this.stacktrace = stacktrace;
		this.appCode = appCode;
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
		return o;
	}
}
