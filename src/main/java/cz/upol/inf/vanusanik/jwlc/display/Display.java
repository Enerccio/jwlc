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
package cz.upol.inf.vanusanik.jwlc.display;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.PointerContainer;

/**
 * WaylandDisplay abstraction wrapper.
 * 
 * @author enerccio
 *
 */
public class Display implements PointerContainer {

	private final Pointer handle;

	protected Display(Pointer handle) {
		this.handle = handle;
	}

	/**
	 * Creates display from provided pointer.
	 * 
	 * Internal use only.
	 * 
	 * @param handle
	 * @return
	 */
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

	/**
	 * Returns wayland display.
	 * 
	 * @return
	 */
	public static Display getDisplay() {
		return from(JWLC.nativeHandler().wlc_get_wl_display());
	}

	/* Setters */

	/* Functionality */

}
