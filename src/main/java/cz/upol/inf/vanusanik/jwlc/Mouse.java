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

import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_button_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_movement_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_scroll_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.touch_callback;
import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.wlc.ButtonState;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers.wlc_modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.TouchType;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerButtonCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerMotionCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerScrollCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerTouchCallback;

/**
 * Pointer related functions are grouped here.
 * This class's name is Mouse because Pointer is used by jna.
 * @author pvan
 *
 */
public class Mouse {
	
	/* Callbacks */
	
	/**
	 * Sets the callback for pointer button click
	 * @param cb
	 */
	public static void setButtonCallback(final PointerButtonCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_pointer_button_cb(new pointer_button_callback() {
			
			public boolean callback(Pointer handle, int time, wlc_modifiers mods, int button, int buttonState, wlc_point point) {
				return cb.onButton(View.from(handle),
						Utils.getUnsignedInt(time), Modifiers.from(mods),
						Utils.getUnsignedInt(button), ButtonState.from(buttonState),
						Point.from(point));
			}
		});
	}
	
	/**
	 * Sets the callback for pointer movement 
	 * @param cb
	 */
	public static void setMotionCallback(final PointerMotionCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_pointer_motion_cb(new pointer_movement_callback() {
			
			public boolean callback(Pointer handle, int time, wlc_point point) {
				return cb.onMotion(View.from(handle), Utils.getUnsignedInt(time), Point.from(point));
			}
		});
	}
	
	public static void setScrollCallback(final PointerScrollCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_pointer_scroll_cb(new pointer_scroll_callback() {
			
			public boolean callback(Pointer view, int time, wlc_modifiers mods, 
					byte axisBits, double[] amount) {
				return cb.onScroll(View.from(view), Utils.getUnsignedInt(time), 
						Modifiers.from(mods), Utils.getUnsignedByte(axisBits), amount);
			}
		});
	}
	
	public static void setTouchCallback(final PointerTouchCallback cb) {
		Assert.assertNotNull(cb);
		
		JWLC.nativeHandler().wlc_set_touch_cb(new touch_callback() {
			
			public boolean callback(Pointer view, int time, wlc_modifiers mods, int touchType, int slot, wlc_point position) {
				return cb.onTouch(View.from(view), Utils.getUnsignedInt(time), 
						Modifiers.from(mods), TouchType.from(touchType), 
						slot, Point.from(position));
			}
		});
	}

	/* Methods */
	/* Getters */
	
	public Point getPosition() {
		wlc_point p = new wlc_point();
		JWLC.nativeHandler().wlc_pointer_get_position(p);
		return Point.from(p);
	}
	
	/* Setters */
	
	/**
	 * Sets the pointer position on screen.
	 * @param position
	 */
	public static void setPointerPosition(Point position) {
		JWLC.nativeHandler().wlc_pointer_set_position(position.to());
	}
}
