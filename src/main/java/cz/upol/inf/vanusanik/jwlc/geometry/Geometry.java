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
package cz.upol.inf.vanusanik.jwlc.geometry;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.geometry.Size.wlc_size;

public class Geometry {

	private Point origin;
	private Size size;

	public Geometry() {

	}

	public Geometry(Point origin, Size size) {
		this.origin = origin;
		this.size = size;
	}

	public Geometry(int x, int y, int w, int h) {
		this.origin = new Point(x, y);
		this.size = new Size(w, h);
	}

	public static Geometry from(wlc_geometry geo) {
		if (geo == null) return null;
		return new Geometry(geo.origin.x, geo.origin.y, geo.size.w, geo.size.h);
	}
	
	public void reset(wlc_geometry geo) {
		origin.setX(geo.origin.x);
		origin.setY(geo.origin.y);
		size.setH(geo.size.h);
		size.setW(geo.size.w);
	}

	public wlc_geometry to() {
		wlc_geometry geo = new wlc_geometry();
		geo.origin = new wlc_point();
		geo.size = new wlc_size();
		geo.origin.x = origin.getX();
		geo.origin.y = origin.getY();
		geo.size.w = size.getW();
		geo.size.h = size.getH();
		return geo;
	}

	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public boolean containOther(Geometry other) {
		return origin.getX() <= other.getOrigin().getX() && origin.getY() <= other.getOrigin().getY()
				&& origin.getX() + size.getW() >= other.getOrigin().getX() + other.getSize().getW()
				&& origin.getY() + size.getH() >= other.getOrigin().getY() + other.getSize().getH();
	}

	public boolean contains(Geometry a, Geometry b) {
		return a.containOther(b);
	}

	@Override
	public String toString() {
		return "Geometry [origin=" + origin + ", size=" + size + "]";
	}

	public static class wlc_geometry extends Structure {

		public wlc_point origin;
		public wlc_size size;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "origin", "size" });
		}

	}
}
