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
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_mask_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_move_view_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.request_move_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.request_resize_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.state_request_callback;
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Resource;
import cz.upol.inf.vanusanik.jwlc.Utils;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.wayland.WaylandClient;
import cz.upol.inf.vanusanik.jwlc.wayland.WaylandClientFactory;
import cz.upol.inf.vanusanik.jwlc.wayland.impl.SimpleWaylandFactory;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewDestroyedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewFocusCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewMoveToOutputCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewPostRenderCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewPreRenderCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewPropertiesUpdatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestGeometryCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestMoveCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestResizeCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewStateRequestCallback;

/**
 * wlc_view abstraction wrapper class.
 * 
 * @author enerccio
 *
 */
public class View extends WLCHandle {

	public static final View INVALID_VIEW = new View(null);

	/**
	 * Holds positioner information. Useful for gtk windows.
	 */
	public final ViewPositioner positioner;

	protected View(Pointer handle) {
		super(handle);
		positioner = new ViewPositioner(this);
	}

	@Override
	public String toString() {
		return "View [getHandle()=" + getHandle() + "]";
	}

	/**
	 * Creates view from provided pointer.
	 * 
	 * Internal use only, use with care.
	 * @param handle
	 * @return
	 */
	public static View from(Pointer handle) {
		if (handle == null)
			return null;
		return new View(handle);
	}

	/* Callbacks */
	
	/**
	 * Sets callback to be called when view is created.
	 * @param cb
	 */
	public static void setCreatedCallback(final ViewCreatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_created_cb(new handle_callback() {

			public boolean callback(Pointer handle) {
				return cb.onViewCreated(View.from(handle));
			}
		});
	}

	/**
	 * Sets callback to be called when view is destroyed.
	 * @param cb
	 */
	public static void setDestroyedCallback(final ViewDestroyedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_view_destroyed_cb(new handle_callback_void() {

					public void callback(Pointer handle) {
						cb.onViewDestroyed(View.from(handle));
					}
				});
	}

	/**
	 * Sets callback to be called when focus changes for view.
	 * @param cb
	 */
	public static void setFocusCallback(final ViewFocusCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_focus_cb(new focus_callback() {

			public void callback(Pointer handle, boolean focus) {
				cb.onFocusChange(View.from(handle), focus);
			}
		});
	}

	/**
	 * Sets callback to be called when view requests to be moved.
	 * Start an interactive move to agree.
	 * @param cb
	 */
	public static void setRequestMoveCallback(
			final ViewRequestMoveCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_view_request_move_cb(new request_move_callback() {

					public void callback(Pointer handle, wlc_point point) {
						cb.onRequestMove(View.from(handle), Point.from(point));
					}
				});
	}

	/**
	 * Sets callback to be called when view requests to be resized.
	 * Start interactive resize to agree.
	 * @param cb
	 */
	public static void setRequestResizeCallback(
			final ViewRequestResizeCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_view_request_resize_cb(new request_resize_callback() {

					public void callback(Pointer handle, int edges,
							wlc_point point) {
						cb.onRequestResize(View.from(handle),
								Utils.getUnsignedInt(edges), Point.from(point));
					}
				});
	}

	/**
	 * Sets callback to be called when view wants to change it's geometry.
	 * Use {@link #setGeometry(long, Geometry)} to agree.
	 * 
	 * WARNING: Use empty callback to prevent windows to automatically change without you 
	 * agreeing to it!
	 * @param cb
	 */
	public static void setRequestGeometry(
			final ViewRequestGeometryCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_view_request_geometry_cb(new geometry_callback() {

					public void callback(Pointer handle, wlc_geometry g) {
						cb.onRequestGeometry(View.from(handle),
								Geometry.from(g));
					}
				});
	}

	/**
	 * Sets callback to be called when view is moved to the output.
	 * @param cb
	 */
	public static void setMoveToOutputCallback(
			final ViewMoveToOutputCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_view_move_to_output_cb(
				new handle_move_view_callback() {

					public void callback(Pointer view, Pointer from,
							Pointer to) {
						cb.onMoveToOutput(View.from(view), Output.from(from),
								Output.from(to));
					}
				});
	}

	/**
	 * Sets callback to be called when view wants to change it's state.
	 * Call {@link #setState(int, boolean)} to agree.
	 * @param cb
	 */
	public static void setStateRequestCallback(
			final ViewStateRequestCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_view_request_state_cb(new state_request_callback() {

					public void callback(Pointer view, int state,
							boolean toggle) {
						cb.onStateRequest(View.from(view), state, toggle);
					}
				});
	}

	/**
	 * Sets callback to be called before view is rendered.
	 * @param cb
	 */
	public static void setPreRenderCallback(final ViewPreRenderCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_view_render_pre_cb(new handle_callback_void() {

					public void callback(Pointer handle) {
						cb.onPreRender(View.from(handle));
					}
				});
	}

	/**
	 * Sets callback to be called when view was just rendered.
	 * @param cb
	 */
	public static void setPostRenderCallback(final ViewPostRenderCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_view_render_post_cb(new handle_callback_void() {

					public void callback(Pointer handle) {
						cb.onPostRender(View.from(handle));
					}
				});
	}

	/**
	 * Sets callback to be called when properties of this view were changed.
	 * @param cb
	 */
	public static void setPropertiesUpdatedCallback(
			final ViewPropertiesUpdatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_view_properties_updated_cb(new handle_mask_callback() {

					public void callback(Pointer view, int uintmask) {
						cb.onPropertiesUpdated(View.from(view),
								Utils.getUnsignedInt(uintmask));
					}

				});
	}

	/* Methods */
	/* Getters */

	/**
	 * @return client geometry of this view.
	 */
	public Geometry getGeometry() {
		return Geometry
				.from(JWLC.nativeHandler().wlc_view_get_geometry(this.to()));
	}

	/**
	 * @return null or parent view.
	 */
	public View getParent() {
		return View.from(JWLC.nativeHandler().wlc_view_get_parent(this.to()));
	}

	/**
	 * @return output of this view.
	 */
	public Output getOutput() {
		return Output.from(JWLC.nativeHandler().wlc_view_get_output(this.to()));
	}

	/**
	 * @return state of this view.
	 */
	public long getState() {
		return Utils.getUnsignedInt(
				JWLC.nativeHandler().wlc_view_get_state(this.to()));
	}

	/**
	 * @return title of this view.
	 */
	public String getTitle() {
		return JWLC.nativeHandler().wlc_view_get_title(this.to());
	}

	/**
	 * @return shell instance (shell-surface only).
	 */
	public String getShellInstance() {
		return JWLC.nativeHandler().wlc_view_get_instance(this.to());
	}

	/**
	 * @return shell class (shell-surface only).
	 */
	public String getShellClass() {
		return JWLC.nativeHandler().wlc_view_get_class(this.to());
	}

	/**
	 * @return app id (xdg-surface only).
	 */
	public String getAppId() {
		return JWLC.nativeHandler().wlc_view_get_app_id(this.to());
	}

	/**
	 * @return pid of process which created this view.
	 */
	public long getPid() {
		return Utils.getUnsignedInt(
				JWLC.nativeHandler().wlc_view_get_pid(this.to()));
	}

	/**
	 * @return type of this view. 
	 * @see {@link ViewType}
	 */
	public long getType() {
		return Utils.getAsUnsignedInt(
				JWLC.nativeHandler().wlc_view_get_type(this.to()));
	}

	/**
	 * @return visible geometry (what server renders).
	 */
	public Geometry getVisibleGeometry() {
		wlc_geometry geo = new wlc_geometry();
		JWLC.nativeHandler().wlc_view_get_visible_geometry(this.to(), geo);
		return Geometry.from(geo);
	}

	/**
	 * @return visiblity view mask.
	 */
	public long getMask() {
		return Utils.getUnsignedInt(
				JWLC.nativeHandler().wlc_view_get_mask(this.to()));
	}

	/**
	 * @return resource of this view.
	 */
	public Resource getResource() {
		return Resource
				.from(JWLC.nativeHandler().wlc_view_get_surface(this.to()));
	}

	private static SimpleWaylandFactory waylandFactory = new SimpleWaylandFactory();
	/**
	 * @return wayland client for this view.
	 */
	public WaylandClient getClient() {
		return getClient(waylandFactory);
	}

	/**
	 * Gets the wayland client for this view.
	 * @param factory to create wrapper class.
	 * @return wrapped wl_client for this view.
	 */
	public WaylandClient getClient(WaylandClientFactory factory) {
		return factory
				.create(JWLC.nativeHandler().wlc_view_get_wl_client(this.to()));
	}

	/**
	 * @return surface role of this view, can be null
	 */
	public Resource getRole() {
		return Resource.from(JWLC.nativeHandler().wlc_view_get_role(this.to()));
	}

	/* Setters */

	/**
	 * Sets mask.
	 * @param mask
	 */
	public void setMask(long mask) {
		JWLC.nativeHandler().wlc_view_set_mask(this.to(),
				Utils.getAsUnsignedInt(mask));
	}

	/**
	 * Sets state.
	 * @param state
	 * @param toggle
	 */
	public void setState(int state, boolean toggle) {
		Assert.assertNotNull(state);

		JWLC.nativeHandler().wlc_view_set_state(this.to(), state, toggle);
	}

	/**
	 * Sets geometry. Set edges if the geometry change is caused by interactive resize.
	 * @param edges
	 * @param geo
	 */
	public void setGeometry(long edges, Geometry geo) {
		Assert.assertNotNull(geo);

		int e = Utils.getAsUnsignedInt(edges);
		wlc_geometry g = geo.to();
		JWLC.nativeHandler().wlc_view_set_geometry(this.to(), e, g);
		geo.reset(g);
	}

	/**
	 * Parent this view to the parent view.
	 * @param parent
	 */
	public void setParent(View parent) {
		if (parent == null)
			parent = INVALID_VIEW;

		JWLC.nativeHandler().wlc_view_set_parent(this.to(), parent.to());
	}

	/**
	 * Set type of the view. Toggle indicates whether it is set or not.
	 * @param type
	 * @param toggle
	 */
	public void setType(long type, boolean toggle) {
		JWLC.nativeHandler().wlc_view_set_type(this.to(),
				Utils.getAsUnsignedInt(type), toggle);
	}

	/**
	 * Assign output to this view.
	 * @param output
	 */
	public void setOutput(Output output) {
		Assert.assertNotNull(output);

		JWLC.nativeHandler().wlc_view_set_output(this.to(), output.to());
	}

	/* Functionality */

	/**
	 * Brings view to the front.
	 */
	public void bringToFront() {
		JWLC.nativeHandler().wlc_view_bring_to_front(this.to());
	}

	/**
	 * Focuses this view.
	 */
	public void focus() {
		JWLC.nativeHandler().wlc_view_focus(this.to());
	}

	/**
	 * Closes this view.
	 */
	public void close() {
		JWLC.nativeHandler().wlc_view_close(this.to());
	}

	/**
	 * Sends this view to back.
	 */
	public void sendToBack() {
		JWLC.nativeHandler().wlc_view_send_to_back(this.to());
	}

	/**
	 * Unfocus all views (equivalent to {@link #INVALID_VIEW}.{@link #focus()}).
	 */
	public static void unfocus() {
		INVALID_VIEW.focus();
	}

	/**
	 * Brings this view above other view.
	 * @param other
	 */
	public void bringAbove(View other) {
		JWLC.nativeHandler().wlc_view_bring_above(this.to(), other.to());
	}

	/**
	 * Brings this view below other view.
	 * @param other
	 */
	public void bringBelow(View other) {
		JWLC.nativeHandler().wlc_view_send_below(this.to(), other.to());
	}
}
