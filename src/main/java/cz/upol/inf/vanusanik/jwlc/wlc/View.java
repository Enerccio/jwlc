package cz.upol.inf.vanusanik.jwlc.wlc;

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
	
	public static final View INVALID_VIEW = new View(0);

	public final ViewPositioner positioner;
	
	protected View(long handle) {
		super(handle);
		positioner = new ViewPositioner(this);
	}
	
	@Override
	public String toString() {
		return "View [getHandle()=" + getHandle() + "]";
	}
	
	public static View from(long handle) {
		if (handle == 0)
			return null;
		return new View(handle);
	}

	public void setGeometry(long edges, Geometry geo) {
		Assert.assertNotNull(geo);
		
		int e = Utils.getAsUnsignedInt(edges);
		wlc_geometry g = geo.to();
		JWLC.nativeHandler().wlc_view_set_geometry(Utils.getAsUnsignedInt(this.to()), e, g);
		geo.reset(g);
	}
	
	public Geometry getGeometry() {
		return Geometry.from(JWLC.nativeHandler().wlc_view_get_geometry(Utils.getAsUnsignedInt(this.to())));
	}

	public View getParent() {
		return View.from(Utils.getUnsignedInt(
				JWLC.nativeHandler().wlc_view_get_parent(Utils.getAsUnsignedInt(this.to()))));
	}
	
	public static void setCreatedCallback(final ViewCreatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_created_cb(new handle_callback() {

			public boolean callback(int handle) {
				return cb.onViewCreated(View.from(Utils.getUnsignedInt(handle)));
			}
		});
	}
	
	public Output getOutput() {
		return Output.from(Utils.getUnsignedInt(JWLC.nativeHandler().wlc_view_get_output(Utils.getAsUnsignedInt(this.to()))));
	}
	
	public void setMask(long mask) {
		JWLC.nativeHandler().wlc_view_set_mask(Utils.getAsUnsignedInt(this.to()), Utils.getAsUnsignedInt(mask));
	}
	
	public void bringToFront() {
		JWLC.nativeHandler().wlc_view_bring_to_front(Utils.getAsUnsignedInt(this.to()));
	}
	
	public void focus() {
		JWLC.nativeHandler().wlc_view_focus(Utils.getAsUnsignedInt(this.to()));
	}
	
	public static void setDestroyedCallback(final ViewDestroyedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_destroyed_cb(new handle_callback_void() {

			public void callback(int handle) {
				cb.onViewDestroyed(View.from(Utils.getUnsignedInt(handle)));
			}
		});
	}
	
	public static void setFocusCallback(final ViewFocusCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_focus_cb(new focus_callback() {

			public void callback(int handle, boolean focus) {
				cb.onFocusChange(View.from(Utils.getUnsignedInt(handle)), focus);
			}			
		});
	}
	
	public void setState(int state, boolean toggle) {
		Assert.assertNotNull(state);
		
		JWLC.nativeHandler().wlc_view_set_state(Utils.getAsUnsignedInt(this.to()), state, toggle);
	}
	
	public static void setRequestMoveCallback(final ViewRequestMoveCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_request_move_cb(new request_move_callback() {

			public void callback(int handle, wlc_point point) {
				cb.onRequestMove(View.from(Utils.getUnsignedInt(handle)), Point.from(point));
			}
		});
	}
	
	public static void setRequestResizeCallback(final ViewRequestResizeCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_request_resize_cb(new request_resize_callback() {

			public void callback(int handle, int edges, wlc_point point) {
				cb.onRequestResize(View.from(Utils.getUnsignedInt(handle)), Utils.getUnsignedInt(edges), Point.from(point));
			}
		});
	}
	
	public static void setRequestGeometry(final ViewRequestGeometryCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_request_geometry_cb(new geometry_callback() {

			public void callback(int handle, wlc_geometry g) {
				cb.onRequestGeometry(View.from(Utils.getUnsignedInt(handle)), Geometry.from(g));
			}
		});
	}

	public void close() {
		JWLC.nativeHandler().wlc_view_close(Utils.getAsUnsignedInt(this.to()));
	}

	public void sendToBack() {
		JWLC.nativeHandler().wlc_view_send_to_back(Utils.getAsUnsignedInt(this.to()));
	}

	public static void unfocus() {
		INVALID_VIEW.focus();
	}
}
