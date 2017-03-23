package cz.upol.inf.vanusanik.jwlc;

import cz.upol.inf.vanusanik.jwlc.Callbacks.keyboard_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.KeyState;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers.wlc_modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.KeyboardCallback;

public class Keyboard {
	
	public static void setKeyboardCallback(final KeyboardCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_keyboard_key_cb(new keyboard_callback() {
			
			public boolean callback(int handle, int time, wlc_modifiers mods, int key, int keyState) {
				cb.onKeyboard(View.from(Utils.getUnsignedInt(handle)), 
						Utils.getUnsignedInt(time), Modifiers.from(mods), 
						Utils.getUnsignedInt(key), KeyState.from(keyState));
				return false;
			}
		});
	}
	
	public static long getSymkeyForKey(long key, Modifiers modifiers) {
		return Utils.getUnsignedInt(JWLC.nativeHandler().wlc_keyboard_get_keysym_for_key(Utils.getAsUnsignedInt(key), 
				modifiers == null ? null : modifiers.to()));
	}

}
