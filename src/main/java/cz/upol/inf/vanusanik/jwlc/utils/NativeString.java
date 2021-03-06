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
package cz.upol.inf.vanusanik.jwlc.utils;

import java.nio.CharBuffer;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Provides a temporary allocation of an immutable C string
 * (<code>const char*</code> or <code>const wchar_t*</code>) for use when
 * converting a Java String into a native memory function argument.
 *
 * @author Todd Fast, todd.fast@sun.com
 * @author twall@users.sf.net
 */
@SuppressWarnings("rawtypes")
public class NativeString implements CharSequence, Comparable {

	private Pointer pointer;
	private boolean wide;

	public NativeString(Pointer p, boolean wide) {
		this.pointer = p;
		this.wide = wide;
	}

	/**
	 * Create a native string (NUL-terminated array of <code>char</code>).
	 * <p>
	 * If the system property <code>jna.encoding</code> is set, its value will
	 * be used to encode the native string. If not set or if the encoding is
	 * unavailable, the default platform encoding will be used.
	 */
	public NativeString(String string) {
		this(string, false);
	}

	/**
	 * Create a native string as a NUL-terminated array of <code>wchar_t</code>
	 * (if <code>wide</code> is true) or <code>char</code>.
	 * <p>
	 * If the system property <code>jna.encoding</code> is set, its value will
	 * be used to encode the native <code>char</code>string. If not set or if
	 * the encoding is unavailable, the default platform encoding will be used.
	 * 
	 * @param string
	 *            value to write to native memory
	 * @param wide
	 *            whether to store the String as <code>wchar_t</code>
	 */
	@SuppressWarnings("deprecation")
	public NativeString(String string, boolean wide) {
		if (string == null) {
			throw new NullPointerException("String must not be null");
		}
		// Allocate the memory to hold the string. Note, we have to
		// make this 1 element longer in order to accommodate the terminating
		// NUL (which is generated in Pointer.setString()).
		this.wide = wide;
		if (wide) {
			int len = (string.length() + 1) * Native.WCHAR_SIZE;
			pointer = new Memory(len);
			pointer.setString(0, string, true);
		} else {
			byte[] data = Native.toByteArray(string);
			pointer = new Memory(data.length + 1);
			pointer.write(0, data, 0, data.length);
			pointer.setByte(data.length, (byte) 0);
		}
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object other) {

		if (other instanceof CharSequence) {
			return compareTo(other) == 0;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public String toString() {
		String s = wide ? "const wchar_t*" : "const char*";
		s += "(" + pointer.getString(0, wide) + ")";
		return s;
	}

	public Pointer getPointer() {
		return pointer;
	}

	public char charAt(int index) {
		return toString().charAt(index);
	}

	public int length() {
		return toString().length();
	}

	public CharSequence subSequence(int start, int end) {
		return CharBuffer.wrap(toString()).subSequence(start, end);
	}

	public int compareTo(Object other) {

		if (other == null)
			return 1;

		return toString().compareTo(other.toString());
	}
}