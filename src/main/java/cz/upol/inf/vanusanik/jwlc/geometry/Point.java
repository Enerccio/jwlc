package cz.upol.inf.vanusanik.jwlc.geometry;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * Implementation of wlc_point
 * 
 * @author pvan
 *
 */
public class Point {

	public static final Point ORIGIN_ZERO = new Point(0, 0);
	public static final Point POINT_ZERO = new Point(0, 0);

	public static Point min(Point a, Point b) {
		return of(Math.min(a.x, b.x), Math.min(a.y, b.y));
	}

	public static Point max(Point a, Point b) {
		return of(Math.max(a.x, b.x), Math.max(a.y, b.y));
	}

	/**
	 * Static factory
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Point of(int x, int y) {
		return new Point(x, y);
	}

	/**
	 * Creates point with provided values
	 * 
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point() {
		this.x = 0;
		this.y = 0;
	}

	private int x, y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	public static Point from(wlc_point point) {
		if (point == null) return null;
		return Point.of(point.x, point.y);
	}

	public wlc_point to() {
		wlc_point point = new wlc_point();
		point.x = x;
		point.y = y;
		return point;
	}

	public static class wlc_point extends Structure {

		public int x, y;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "x", "y" });
		}

	}
}
