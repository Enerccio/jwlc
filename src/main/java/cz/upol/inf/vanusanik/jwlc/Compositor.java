package cz.upol.inf.vanusanik.jwlc;

import cz.upol.inf.vanusanik.jwlc.Callbacks.void_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.CompositorReadyCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.CompositorTerminatedCallback;

public class Compositor {

	public static void setReadyCallback(final CompositorReadyCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_compositor_ready_cb(new void_callback() {
			
			public void callback() {
				cb.onReady();
			}
		});
	}
	
	public static void setTerminatedCallback(final CompositorTerminatedCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_compositor_terminate_cb(new void_callback() {
			
			public void callback() {
				cb.onTerminated();
			}
		});
	}
	
}
