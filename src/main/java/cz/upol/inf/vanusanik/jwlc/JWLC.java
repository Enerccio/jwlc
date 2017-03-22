package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Native;

import cz.upol.inf.vanusanik.jwlc.Callbacks.logger_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.LogType;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.LoggerCallback;

public class JWLC implements Runnable {

	private static WLC instance;
	
	public static WLC nativeHandler() {
		if (instance == null) {
			synchronized (JWLC.class) {
				instance = (WLC) Native.loadLibrary("wlc", WLC.class);
			}
		}
		return instance;
	}

	public static JWLC getJWLCHandler() throws Exception {
		return new JWLC();
	}

	/**
	 * Initialize wlc. Returns false on failure.
	 */
	public void init() {
		if (!nativeHandler().wlc_init()) {
			throw new RuntimeException("failed to init wlc");
		}
	}

	public void terminate() {
		if (instance != null) {
			synchronized (JWLC.class) {
				if (instance != null) {
					instance.wlc_terminate();
					instance = null;
				}
			}
		}
	}

	public void run() {
		nativeHandler().wlc_run();
	}

	public void setLoggerCallback(final LoggerCallback cb) {
		Assert.assertNotNull(cb);

		nativeHandler().wlc_log_set_handler(new logger_callback() {

			public void callback(int type, String message) {
				cb.onLog(LogType.from(type), message);
			}
		});

	}
}
