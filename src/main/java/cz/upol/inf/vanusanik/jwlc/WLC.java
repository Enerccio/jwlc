package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Library;

import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;

public interface WLC extends Library {

	// Renderer

	void wlc_pixels_write(int pixel_format, wlc_geometry geometry, Object data);

	// main

}
