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

import java.util.AbstractList;
import java.util.List;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.wlc.View;

/**
 * Provides mutable view list. The actual get() values can change across the
 * calls as provider's pointer memory changes. As it does not handle resizes,
 * you need to replace this view instance with new instance each time number of
 * views change for the output.
 * 
 * This class is very unsafe! Handle with care, can crash your jvm.
 */
public class MutableViewList extends AbstractList<View> implements List<View> {

	private Pointer pointer;
	private int length;

	public MutableViewList(Pointer p, int len) {
		this.pointer = p;
		this.length = len;
	}

	public View get(int arg0) {
		if (arg0 < 0 || arg0 >= length)
			throw new ArrayIndexOutOfBoundsException(arg0);
		return View.from(pointer.getPointer(arg0 * Pointer.SIZE));
	}

	public int size() {
		return length;
	}
}
