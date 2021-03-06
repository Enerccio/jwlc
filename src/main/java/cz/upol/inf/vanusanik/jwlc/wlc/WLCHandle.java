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
package cz.upol.inf.vanusanik.jwlc.wlc;

import java.security.InvalidParameterException;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.Assert;
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.PointerContainer;
import cz.upol.inf.vanusanik.jwlc.utils.NativeString;

/**
 * Base wlc_handle abstraction wrapper class.
 * 
 * @author enerccio
 *
 */
public class WLCHandle implements PointerContainer {

	private final Pointer handle;

	protected WLCHandle(Pointer handle) {
		this.handle = handle;
	}

	public static WLCHandle from(Pointer handle) {
		if (handle == null)
			return null;
		return new WLCHandle(handle);
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
		WLCHandle other = (WLCHandle) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WLCHandle [handle=" + handle + "]";
	}

	/**
	 * Returns custom data from this handle. XXX: Experimental
	 * 
	 * @param tclass
	 * @return
	 */
	public <T> T getCustomData(Class<T> tclass) {
		Pointer nativeData = getCustomDataPointer();
		return convertNativeData(nativeData, tclass);
	}

	@SuppressWarnings("unchecked")
	static <T> T convertNativeData(Pointer nativeData, Class<?> tclass) {
		if (tclass.isAssignableFrom(WLCHandle.class)) {
			if (tclass == Output.class)
				return (T) Output.from(nativeData);
			if (tclass == View.class)
				return (T) View.from(nativeData);
			return (T) from(nativeData);
		}
		if (String.class == tclass) {
			return (T) nativeData.getString(0);
		}
		if (Number.class.isAssignableFrom(tclass)) {
			if (tclass == Byte.class) {
				return (T) Byte.valueOf(nativeData.getByte(0));
			}
			if (tclass == Short.class) {
				return (T) Short.valueOf(nativeData.getShort(0));
			}
			if (tclass == Integer.class) {
				return (T) Integer.valueOf(nativeData.getInt(0));
			}
			if (tclass == Long.class) {
				return (T) Long.valueOf(nativeData.getLong(0));
			}
			if (tclass == Float.class) {
				return (T) Float.valueOf(nativeData.getFloat(0));
			}
			if (tclass == Double.class) {
				return (T) Double.valueOf(nativeData.getDouble(0));
			}
		}
		if (tclass.isAssignableFrom(Pointer.class))
			return (T) nativeData;
		throw new InvalidParameterException("referenced not found");
	}

	private Pointer getCustomDataPointer() {
		return JWLC.nativeHandler().wlc_handle_get_user_data(this.to());
	}

	/**
	 * Sets custom data to this handle. XXX: Experimental
	 * 
	 * @param value
	 */
	public <T> void setCustomData(T value) {
		Assert.assertNotNull(value);

		Pointer p = convertToPointer(value);
		JWLC.nativeHandler().wlc_handle_set_user_data(this.to(), p);
	}

	static <T> Pointer convertToPointer(T value) {
		if (value instanceof WLCHandle)
			return ((WLCHandle) value).to();
		if (value instanceof String)
			return new NativeString((String) value, false).getPointer();
		if (value instanceof Number) {
			if (value instanceof Byte) {
				return new Pointer(((Byte) value).longValue());
			}
			if (value instanceof Short) {
				return new Pointer(((Short) value).longValue());
			}
			if (value instanceof Integer) {
				return new Pointer(((Integer) value).longValue());
			}
			if (value instanceof Long) {
				return new Pointer(((Long) value).longValue());
			}
			if (value instanceof Float) {
				return new Pointer(Float.floatToRawIntBits(((Float) value)));
			}
			if (value instanceof Double) {
				return new Pointer(
						Double.doubleToRawLongBits(((Double) value)));
			}
		}
		if (value instanceof Pointer) {
			return (Pointer) value;
		}
		throw new InvalidParameterException("unknown type");
	}
}
