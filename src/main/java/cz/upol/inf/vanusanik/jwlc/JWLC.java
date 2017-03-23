/*
 * MIT License
 * 
 * Copyright (c) 2017 Peter Vaňušanik
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

	public static void exec(String bin, String[] args) {
		Assert.assertNotNull(bin);
		Assert.assertNotNull(args);
		
		nativeHandler().wlc_exec(bin, args);
	}
}
