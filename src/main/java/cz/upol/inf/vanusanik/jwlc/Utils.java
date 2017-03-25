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
package cz.upol.inf.vanusanik.jwlc;

import java.io.FileDescriptor;
import java.lang.reflect.Field;

public class Utils {

	/**
	 * Converts from int to unsigned int stored in long
	 * 
	 * @param x
	 * @return
	 */
	public static long getUnsignedInt(int x) {
		return Integer.toUnsignedLong(x);
	}

	/**
	 * Converts from unsigned int stored in long to unsigned int stored in int
	 * 
	 * @param x
	 * @return
	 */
	public static int getAsUnsignedInt(long x) {
		return (int) (x & 0xffffffffL);
	}

	public static short getUnsignedByte(byte x) {
		return (short) Byte.toUnsignedInt(x);
	}

	public static byte getAsUnsignedByte(short x) {
		return (byte) (x & 0xff);
	}

	public static int getUnsignedShort(short x) {
		return Short.toUnsignedInt(x);
	}

	public static short getAsUnsignedShort(int x) {
		return (short) (x & 0xffff);
	}

	public static int getFD(FileDescriptor fd) {
		try {
			Field f = FileDescriptor.class.getDeclaredField("fd");
			f.setAccessible(true);
			return f.getInt(fd);
		} catch (Exception e) {
		
		}
		return 0;
	}
	
	public static FileDescriptor createFD(int fd) {
		try {
			FileDescriptor desc = new FileDescriptor();
			Field f = FileDescriptor.class.getDeclaredField("fd");
			f.setAccessible(true);
			f.set(desc, fd);
			return desc;
		} catch (Exception e) {
			return null;
		}
	}
}
