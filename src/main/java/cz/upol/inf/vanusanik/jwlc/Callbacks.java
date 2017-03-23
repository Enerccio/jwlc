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

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.geometry.Size.wlc_size;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers.wlc_modifiers;

public class Callbacks {

	public interface handle_callback extends Callback {
		boolean callback(Pointer handle);
	}
	
	public interface handle_callback_void extends Callback {
		void callback(Pointer handle);
	}
	
	public interface focus_callback extends Callback {
		void callback(Pointer handle, boolean focus);
	}
	
	public interface logger_callback extends Callback {
		void callback(int type, String message);
	}
	
	public interface output_resolution_callback extends Callback {
		void callback(Pointer handle, wlc_size fromSize, wlc_size toSize);
	}
	
	public interface request_move_callback extends Callback {
		void callback(Pointer handle, wlc_point point);
	}
	
	public interface request_resize_callback extends Callback {
		void callback(Pointer handle, int edges, wlc_point point);
	}
	
	public interface geometry_callback extends Callback {
		void callback(Pointer handle, wlc_geometry g);
	}
	
	public interface keyboard_callback extends Callback {
		boolean callback(Pointer handle, int time, wlc_modifiers mods, int key, int keyState);
	}
	
	public interface pointer_button_callback extends Callback {
		boolean callback(Pointer handle, int time, wlc_modifiers mods, int button, int buttonState, wlc_point point);
	}
	
	public interface pointer_movement_callback extends Callback {
		boolean callback(Pointer handle, int time, wlc_point point);
	}
}
