package cz.upol.inf.vanusanik.jwlc.display;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.PointerContainer;

public class Display implements PointerContainer {
	
	private final Pointer handle;

	protected Display(Pointer handle) {
		this.handle = handle;
	}

	public static Display from(Pointer handle) {
		if (handle == null)
			return null;
		return new Display(handle);
	}

	public Pointer to() {
		return handle;
	}

	public Pointer getHandle() {
		return handle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
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
		Display other = (Display) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Display [handle=" + handle + "]";
	}

	/* Methods */
	/* Getters */
	
	public static Display getDisplay() {
		return from(JWLC.nativeHandler().wlc_get_wl_display());
	}
	
	/* Setters */
	
	/* Functionality */
	
}