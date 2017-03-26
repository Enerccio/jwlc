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

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import cz.upol.inf.vanusanik.jwlc.Callbacks.keyboard_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.KeyState;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers.wlc_modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.KeyboardCallback;

/**
 * libwlc keyboard related functions/callbacks
 * 
 * @author pvan
 *
 */
public class Keyboard {

	/**
	 * Sets the keyboard key callback
	 * 
	 * @param cb
	 */
	public static void setKeyboardCallback(final KeyboardCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_keyboard_key_cb(new keyboard_callback() {

			public boolean callback(Pointer handle, int time,
					wlc_modifiers mods, int key, int keyState) {
				return cb.onKeyboard(View.from(handle),
						Utils.getUnsignedInt(time), Modifiers.from(mods),
						Utils.getUnsignedInt(key), KeyState.from(keyState));
			}

		});
	}

	/**
	 * Utility method to convert raw keycode to keysym. Passed modifiers may
	 * transform the key.
	 * 
	 * @param key
	 * @param modifiers
	 * @return symkey
	 */
	public static long getSymkeyForKey(long key, Modifiers modifiers) {
		return Utils.getUnsignedInt(JWLC.nativeHandler()
				.wlc_keyboard_get_keysym_for_key(Utils.getAsUnsignedInt(key),
						modifiers == null ? null : modifiers.to()));
	}

	public static long getUtf32CharacterForKey(long key, Modifiers modifiers) {
		return Utils.getUnsignedInt(
				JWLC.nativeHandler().wlc_keyboard_get_utf32_for_key(
						Utils.getAsUnsignedInt(key), modifiers.to()));
	}

	public static char[] getUtf32CharsForKey(long key, Modifiers modifiers) {
		return Character.toChars(
				Utils.getAsUnsignedInt(getSymkeyForKey(key, modifiers)));
	}

	public static long[] getCurrentlyHeldKeys() {
		IntByReference ref = new IntByReference();
		Pointer buffer = JWLC.nativeHandler().wlc_keyboard_get_current_key(ref);
		long[] keys = new long[ref.getValue()];
		for (int i = 0; i < keys.length; i++)
			keys[i] = Utils.getUnsignedInt(buffer.getInt(i * 4));
		return keys;
	}
}
