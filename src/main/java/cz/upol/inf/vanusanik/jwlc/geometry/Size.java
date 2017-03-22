package cz.upol.inf.vanusanik.jwlc.geometry;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * Implementation of wlc_size
 * 
 * @author pvan
 *
 */
public class Size {

	/**
	 * Static factory
	 * 
	 * @param w
	 * @param h
	 * @return
	 */
	public static Size of(int w, int h) {
		return new Size(w, h);
	}

	public static Size min(Size a, Size b) {
		return of(Math.min(a.w, b.w), Math.min(a.h, b.h));
	}

	public static Size max(Size a, Size b) {
		return of(Math.max(a.w, b.h), Math.max(a.w, b.h));
	}

	/**
	 * Creates size with provided values
	 * 
	 * @param x
	 * @param y
	 */
	public Size(int w, int h) {
		this.w = w;
		this.h = h;
	}

	public Size() {
		this.w = 0;
		this.h = 0;
	}

	private int w, h;

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + w;
		result = prime * result + h;
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
		Size other = (Size) obj;
		if (w != other.w)
			return false;
		if (h != other.h)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Size [w=" + w + ", h=" + h + "]";
	}

	public static Size from(wlc_size size) {
		if (size == null) return null;
		return Size.of(size.w, size.h);
	}

	public wlc_size to() {
		wlc_size size = new wlc_size();
		size.w = w;
		size.h = h;
		return size;
	}

	public static class wlc_size extends Structure {

		public int w, h;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "w", "h" });
		}

	}
}
