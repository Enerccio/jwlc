package cz.upol.inf.vanusanik.jwlc.utils;

import java.util.AbstractList;
import java.util.List;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.wlc.View;

/**
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
