package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_button_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_movement_callback;
import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.wlc.ButtonState;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers.wlc_modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerButtonCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerMotionCallback;

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
	
	public static void setPointerMotionCallback(final PointerMotionCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_pointer_motion_cb(new pointer_movement_callback() {
			
			public boolean callback(Pointer handle, int time, wlc_point point) {
				return cb.onPointerMotion(View.from(handle), Utils.getUnsignedInt(time), Point.from(point));
			}
		});
	}

	public static void setPointerPosition(Point position) {
		JWLC.nativeHandler().wlc_pointer_set_position(position.to());
	}
}
