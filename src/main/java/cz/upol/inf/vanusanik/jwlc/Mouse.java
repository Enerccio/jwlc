package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_button_callback;
import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.wlc.ButtonState;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers.wlc_modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerButtonCallback;

public class Mouse {
	
	public static void setPointerButtonCallback(final PointerButtonCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_pointer_button_cb(new pointer_button_callback() {
			
			public boolean callback(Pointer handle, int time, wlc_modifiers mods, int button, int buttonState, wlc_point point) {
				return cb.onPointerButton(View.from(handle),
						Utils.getUnsignedInt(time), Modifiers.from(mods),
						Utils.getUnsignedInt(button), ButtonState.from(buttonState),
						Point.from(point));
			}
		});
	}

}
