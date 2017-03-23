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
