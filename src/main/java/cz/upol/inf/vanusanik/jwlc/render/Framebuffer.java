package cz.upol.inf.vanusanik.jwlc.render;

import com.sun.jna.Memory;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;

public class Framebuffer {

	public static void writePixels(PixelFormat fmt, Geometry geo, byte[] data) {
		Memory m = new Memory(data.length);
		m.write(0, data, 0, data.length);
		
		JWLC.nativeHandler().wlc_pixels_write(fmt.to(), geo.to(), m);
	}
	
}
