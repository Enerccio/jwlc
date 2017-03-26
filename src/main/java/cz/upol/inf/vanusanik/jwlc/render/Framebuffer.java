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
		Memory m = new Memory(requestGeometry.getSize().getH()
				* requestGeometry.getSize().getW() * 4);

		wlc_geometry g = new wlc_geometry();
		JWLC.nativeHandler().wlc_pixels_read(fmt.to(), requestGeometry.to(), g,
				m);
		requestGeometry.reset(g);

		byte[] data = new byte[requestGeometry.getSize().getH()
				* requestGeometry.getSize().getW() * 4];
		m.read(0, data, 0, data.length);
		return data;
	}

	public static void drawSurface(Resource resource, Geometry geometry) {
		JWLC.nativeHandler().wlc_surface_render(resource.to(), geometry.to());
	}

}
