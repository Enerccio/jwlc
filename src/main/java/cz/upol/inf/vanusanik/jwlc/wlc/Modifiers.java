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

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class Modifiers {

	private int leds, mods;

	public Modifiers() {

	}

	public Modifiers(int leds, int mods) {
		this.leds = leds;
		this.mods = mods;
	}

	public int getLeds() {
		return leds;
	}

	public void setLeds(int leds) {
		this.leds = leds;
	}

	public int getMods() {
		return mods;
	}

	public void setMods(int mods) {
		this.mods = mods;
	}

	public wlc_modifiers to() {
		wlc_modifiers m = new wlc_modifiers();
		m.leds = leds;
		m.mods = mods;
		return m;
	}

	public static Modifiers from(wlc_modifiers mods) {
		if (mods == null)
			return null;
		return new Modifiers(mods.leds, mods.mods);
	}

	public static class wlc_modifiers extends Structure {

		public int leds, mods;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "leds", "mods" });
		}

	}
}
