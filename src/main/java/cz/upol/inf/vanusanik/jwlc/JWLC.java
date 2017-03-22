package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Native;

import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.render.PixelFormat;
import junit.framework.Assert;

public class JWLC {

	private static WLC instance;

	public static JWLC init() throws Exception {
		if (instance == null) {
			synchronized (JWLC.class) {
				instance = (WLC) Native.loadLibrary("wlc", WLC.class);
			}
		}
		return new JWLC();
	}

	// Renderer

	public void pixelsWrite(PixelFormat fmt, Geometry geometry, byte[] data) {
		Assert.assertNotNull(fmt);
		Assert.assertNotNull(geometry);
		Assert.assertNotNull(data);

		instance.wlc_pixels_write(fmt.to(), geometry.to(), data);
	}

}
