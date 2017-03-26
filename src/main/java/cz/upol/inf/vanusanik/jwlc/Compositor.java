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

import cz.upol.inf.vanusanik.jwlc.Callbacks.void_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.CompositorReadyCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.CompositorTerminatedCallback;

/**
 * Compositor related events.
 * 
 * @author enerccio
 */
public class Compositor {

	private Compositor() {
	}

	/**
	 * Set callback will be called when server is ready to accept clients.
	 * 
	 * @param cb
	 */
	public static void setReadyCallback(final CompositorReadyCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_compositor_ready_cb(new void_callback() {

			public void callback() {
				cb.onReady();
			}
		});
	}

	/**
	 * Set callback will be called when server is about to be terminated.
	 * 
	 * @param cb
	 */
	public static void setTerminatedCallback(
			final CompositorTerminatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_compositor_terminate_cb(new void_callback() {

					public void callback() {
						cb.onTerminated();
					}
				});
	}

}
