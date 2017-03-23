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

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.Assert;
import cz.upol.inf.vanusanik.jwlc.Callbacks.focus_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.geometry_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_callback_void;
import cz.upol.inf.vanusanik.jwlc.Callbacks.request_move_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.request_resize_callback;
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Utils;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewDestroyedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewFocusCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestGeometryCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestMoveCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestResizeCallback;

public class View extends WLCHandle {
	
	public static final View INVALID_VIEW = new View(null);

	public final ViewPositioner positioner;
	
	protected View(Pointer handle) {
		super(handle);
		positioner = new ViewPositioner(this);
	}
	
	@Override
	public String toString() {
		return "View [getHandle()=" + getHandle() + "]";
	}
	
	public static View from(Pointer handle) {
		if (handle == null)
			return null;
		return new View(handle);
	}

	public void setGeometry(long edges, Geometry geo) {
		Assert.assertNotNull(geo);
		
		int e = Utils.getAsUnsignedInt(edges);
		wlc_geometry g = geo.to();
		JWLC.nativeHandler().wlc_view_set_geometry(this.to(), e, g);
		geo.reset(g);
	}
	
	public Geometry getGeometry() {
		return Geometry.from(JWLC.nativeHandler().wlc_view_get_geometry(this.to()));
	}

	public View getParent() {
		return View.from(JWLC.nativeHandler().wlc_view_get_parent(this.to()));
	}
	
	public static void setCreatedCallback(final ViewCreatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_created_cb(new handle_callback() {

			public boolean callback(Pointer handle) {
				return cb.onViewCreated(View.from(handle));
			}
		});
	}
	
	public Output getOutput() {
		return Output.from(JWLC.nativeHandler().wlc_view_get_output(this.to()));
	}
	
	public void setMask(long mask) {
		JWLC.nativeHandler().wlc_view_set_mask(this.to(), Utils.getAsUnsignedInt(mask));
	}
	
	public void bringToFront() {
		JWLC.nativeHandler().wlc_view_bring_to_front(this.to());
	}
	
	public void focus() {
		JWLC.nativeHandler().wlc_view_focus(this.to());
	}
	
	public static void setDestroyedCallback(final ViewDestroyedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_destroyed_cb(new handle_callback_void() {

			public void callback(Pointer handle) {
				cb.onViewDestroyed(View.from(handle));
			}
		});
	}
	
	public static void setFocusCallback(final ViewFocusCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_focus_cb(new focus_callback() {

			public void callback(Pointer handle, boolean focus) {
				cb.onFocusChange(View.from(handle), focus);
			}			
		});
	}
	
	public void setState(int state, boolean toggle) {
		Assert.assertNotNull(state);
		
		JWLC.nativeHandler().wlc_view_set_state(this.to(), state, toggle);
	}
	
	public static void setRequestMoveCallback(final ViewRequestMoveCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_request_move_cb(new request_move_callback() {

			public void callback(Pointer handle, wlc_point point) {
				cb.onRequestMove(View.from(handle), Point.from(point));
			}
		});
	}
	
	public static void setRequestResizeCallback(final ViewRequestResizeCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_request_resize_cb(new request_resize_callback() {

			public void callback(Pointer handle, int edges, wlc_point point) {
				cb.onRequestResize(View.from(handle), Utils.getUnsignedInt(edges), Point.from(point));
			}
		});
	}
	
	public static void setRequestGeometry(final ViewRequestGeometryCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_request_geometry_cb(new geometry_callback() {

			public void callback(Pointer handle, wlc_geometry g) {
				cb.onRequestGeometry(View.from(handle), Geometry.from(g));
			}
		});
	}

	public void close() {
		JWLC.nativeHandler().wlc_view_close(this.to());
	}

	public void sendToBack() {
		JWLC.nativeHandler().wlc_view_send_to_back(this.to());
	}

	public static void unfocus() {
		INVALID_VIEW.focus();
	}
}
