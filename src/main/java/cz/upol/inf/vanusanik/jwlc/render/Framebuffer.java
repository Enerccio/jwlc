package cz.upol.inf.vanusanik.jwlc.render;

import com.sun.jna.Memory;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Resource;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;

public class Framebuffer {

	public static void writePixels(PixelFormat fmt, Geometry geo, byte[] data) {
		Memory m = new Memory(data.length);
		m.write(0, data, 0, data.length);
		
		JWLC.nativeHandler().wlc_pixels_write(fmt.to(), geo.to(), m);
	}
	
	public static byte[] readPixels(PixelFormat fmt, Geometry requestGeometry) {
		Memory m = new Memory(requestGeometry.getSize().getH() * 
				requestGeometry.getSize().getW() * 4);
		
		wlc_geometry g = new wlc_geometry();
		JWLC.nativeHandler().wlc_pixels_read(fmt.to(), requestGeometry.to(), g, m);
		requestGeometry.reset(g);
		
		byte[] data = new byte[requestGeometry.getSize().getH() * 
		                       requestGeometry.getSize().getW() * 4];
		m.read(0, data, 0, data.length);
		return data;
	}
	
	public static void drawSurface(Resource resource, Geometry geometry) {
		JWLC.nativeHandler().wlc_surface_render(resource.to(), geometry.to());
	}
	
}
