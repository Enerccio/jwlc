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

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import cz.upol.inf.vanusanik.jwlc.Callbacks.fd_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.focus_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.geometry_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_callback_void;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_mask_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_move_view_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.keyboard_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.libinput_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.libinput_callback_void;
import cz.upol.inf.vanusanik.jwlc.Callbacks.logger_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.output_resolution_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_button_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_movement_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.pointer_scroll_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.request_move_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.request_resize_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.state_request_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.timer_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.touch_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.void_callback;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.geometry.Size.wlc_size;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers.wlc_modifiers;

public interface WLC extends Library {

	// Wlc.h
	/** -- Callbacks API */

	/**
	 * Output was created. Return false if you want to destroy the output. (e.g.
	 * failed to allocate data related to view)
	 */
	void wlc_set_output_created_cb(handle_callback cb);

	/** Output was destroyed. */
	void wlc_set_output_destroyed_cb(handle_callback_void cb);

	/** Output got or lost focus. */
	void wlc_set_output_focus_cb(focus_callback cb);

	/** Output resolution changed. */
	void wlc_set_output_resolution_cb(output_resolution_callback cb);

	/** Output pre render hook. */
	void wlc_set_output_render_pre_cb(handle_callback_void cb);

	/** Output post render hook. */
	void wlc_set_output_render_post_cb(handle_callback_void cb);

	/**
	 * Output context is created. This generally happens on startup and when
	 * current tty changes
	 */
	void wlc_set_output_context_created_cb(handle_callback_void cb);

	/** Output context was destroyed. */
	void wlc_set_output_context_destroyed_cb(handle_callback_void cb);

	/**
	 * View was created. Return false if you want to destroy the view. (e.g.
	 * failed to allocate data related to view)
	 */
	void wlc_set_view_created_cb(handle_callback cb);

	/** View was destroyed. */
	void wlc_set_view_destroyed_cb(handle_callback_void cb);

	/** View got or lost focus. */
	void wlc_set_view_focus_cb(focus_callback cb);

	/** View was moved to output. */
	void wlc_set_view_move_to_output_cb(handle_move_view_callback cb);

	/**
	 * Request to set given geometry for view. Apply using wlc_view_set_geometry
	 * to agree.
	 */
	void wlc_set_view_request_geometry_cb(geometry_callback cb);

	/**
	 * Request to disable or enable the given state for view. Apply using
	 * wlc_view_set_state to agree.
	 */
	void wlc_set_view_request_state_cb(state_request_callback cb);

	/** Request to move itself. Start a interactive move to agree. */
	void wlc_set_view_request_move_cb(request_move_callback cb);

	/**
	 * Request to resize itself with the given edges. Start a interactive resize
	 * to agree.
	 */
	void wlc_set_view_request_resize_cb(request_resize_callback cb);

	/** View pre render hook. */
	void wlc_set_view_render_pre_cb(handle_callback_void cb);

	/** View post render hook. */
	void wlc_set_view_render_post_cb(handle_callback_void cb);

	/** View properties (title, class, app_id) was updated */
	void wlc_set_view_properties_updated_cb(handle_mask_callback cb);

	/**
	 * Key event was triggered, view handle will be zero if there was no focus.
	 * Return true to prevent sending the event to clients.
	 */
	void wlc_set_keyboard_key_cb(keyboard_callback cb);

	/**
	 * Button event was triggered, view handle will be zero if there was no
	 * focus. Return true to prevent sending the event to clients.
	 */
	void wlc_set_pointer_button_cb(pointer_button_callback cb);

	/**
	 * Scroll event was triggered, view handle will be zero if there was no
	 * focus. Return true to prevent sending the event to clients.
	 */
	void wlc_set_pointer_scroll_cb(pointer_scroll_callback cb);

	/**
	 * Motion event was triggered, view handle will be zero if there was no
	 * focus. Apply with wlc_pointer_set_position to agree. Return true to
	 * prevent sending the event to clients.
	 */
	void wlc_set_pointer_motion_cb(pointer_movement_callback cb);

	/**
	 * Touch event was triggered, view handle will be zero if there was no
	 * focus. Return true to prevent sending the event to clients.
	 */
	void wlc_set_touch_cb(touch_callback cb);

	/** Compositor is ready to accept clients. */
	void wlc_set_compositor_ready_cb(void_callback cb);

	/** Compositor is about to terminate */
	void wlc_set_compositor_terminate_cb(void_callback cb);

	/**
	 * Input device was created. Return value does nothing. (Experimental)
	 */
	void wlc_set_input_created_cb(libinput_callback cb);

	/** Input device was destroyed. (Experimental) */
	void wlc_set_input_destroyed_cb(libinput_callback_void cb);

	/** -- Core API */

	/** Set log handler. Can be set before wlc_init. */
	void wlc_log_set_handler(logger_callback cb);

	/**
	 * Initialize wlc. Returns false on failure.
	 *
	 * Avoid running unverified code before wlc_init as wlc compositor may be
	 * run with higher privileges on non logind systems where compositor binary
	 * needs to be suid.
	 *
	 * wlc_init's purpose is to initialize and drop privileges as soon as
	 * possible.
	 *
	 * Callbacks should be set using wlc_set_*_cb functions before calling
	 * wlc_init, failing to do so will cause any callback the init may trigger
	 * to not be called.
	 */
	boolean wlc_init();

	/** Terminate wlc. */
	boolean wlc_terminate();

	/** Query backend wlc is using. */
	int wlc_get_backend_type();

	/** Exec program. */
	void wlc_exec(String bin, String[] args);

	/** Run event loop. */
	void wlc_run();

	/** Link custom data to handle. */
	void wlc_handle_set_user_data(Pointer handle, Pointer userdata);

	/** Get linked custom data from handle. */
	Pointer wlc_handle_get_user_data(Pointer handle);

	/**
	 * Add fd to event loop. Return value of callback is unused, you should
	 * return 0.
	 */
	Pointer wlc_event_loop_add_fd(int fd, int mask, fd_callback cb,
			Pointer data);

	/**
	 * Add timer to event loop. Return value of callback is unused, you should
	 * return 0.
	 */
	Pointer wlc_event_loop_add_timer(timer_callback cb, Pointer data);

	/** Update timer to trigger after delay. Returns true on success. */
	boolean wlc_event_source_timer_update(Pointer source, int msdelay);

	/** Remove event source from event loop. */
	void wlc_event_source_remove(Pointer source);

	/** -- Output API */

	/**
	 * Get outputs. Returned array is a direct reference, careful when moving
	 * and destroying handles.
	 */
	Pointer wlc_get_outputs(IntByReference memb);

	/** Get focused output. */
	Pointer wlc_get_focused_output();

	/** Get output name. */
	String wlc_output_get_name(Pointer output);

	/** Get sleep state. */
	boolean wlc_output_get_sleep(Pointer output);

	/** Wake up / sleep. */
	void wlc_output_set_sleep(Pointer output, boolean sleep);

	/** Set gamma. R, G, and B are color ramp arrays of size elements. */
	void wlc_output_set_gamma(Pointer output, short size, short[] r, short[] g,
			short[] b);

	/** Get gamma size */
	short wlc_output_get_gamma_size(Pointer output);

	/**
	 * Get real resolution. Resolution applied by either
	 * wlc_output_set_resolution call or initially. Do not use this for
	 * coordinate boundary.
	 */
	wlc_size wlc_output_get_resolution(Pointer output);

	/**
	 * Get virtual resolution. Resolution with transformations applied for
	 * proper rendering for example on high density displays. Use this to figure
	 * out coordinate boundary.
	 */
	wlc_size wlc_output_get_virtual_resolution(Pointer output);

	//
	/** Set resolution. */
	void wlc_output_set_resolution(Pointer output, wlc_size resolution,
			int scale);

	/** Get scale factor. */
	int wlc_output_get_scale(Pointer output);

	/** Get current visibility bitmask. */
	int wlc_output_get_mask(Pointer output);

	/** Set visibility bitmask. */
	void wlc_output_set_mask(Pointer output, int mask);

	/**
	 * Get views in stack order. Returned array is a direct reference, careful
	 * when moving and destroying handles.
	 */
	Pointer wlc_output_get_views(Pointer output, IntByReference memb);

	/**
	 * Get mutable views in creation order. Returned array is a direct
	 * reference, careful when moving and destroying handles. This is mainly
	 * useful for wm's who need another view stack for inplace sorting. For
	 * example tiling wms, may want to use this to keep their tiling order
	 * separated from floating order.
	 */
	Pointer wlc_output_get_mutable_views(Pointer output, IntByReference ref);

	/**
	 * Set views in stack order. This will also change mutable views. Returns
	 * false on failure.
	 */
	boolean wlc_output_set_views(Pointer output, Pointer[] views, int memb);

	/** Focus output. Pass zero for no focus. */
	void wlc_output_focus(Pointer output);

	/** -- View API */

	/** Focus view. Pass zero for no focus. */
	void wlc_view_focus(Pointer view);

	/** Close view. */
	void wlc_view_close(Pointer view);

	/** Get current output. */
	Pointer wlc_view_get_output(Pointer view);

	/** Set output. Alternatively you can wlc_output_set_views. */
	void wlc_view_set_output(Pointer view, Pointer output);

	/** Send behind everything. */
	void wlc_view_send_to_back(Pointer view);

	/** Send below another view. */
	void wlc_view_send_below(Pointer view, Pointer other);

	/** Send above another view. */
	void wlc_view_bring_above(Pointer view, Pointer other);

	/** Bring to front of everything. */
	void wlc_view_bring_to_front(Pointer view);

	/** Get current visibility bitmask. */
	int wlc_view_get_mask(Pointer view);

	/** Set visibility bitmask. */
	void wlc_view_set_mask(Pointer view, int mask);

	/** Get current geometry. (what client sees) */
	wlc_geometry wlc_view_get_geometry(Pointer view);

	/**
	 * Get size requested by positioner, as defined in xdg-shell v6. Returns
	 * NULL if view has no valid positioner
	 */
	wlc_size wlc_view_positioner_get_size(Pointer view);

	/**
	 * Get anchor rectangle requested by positioner, as defined in xdg-shell v6.
	 * Returns NULL if view has no valid positioner.
	 */
	wlc_geometry wlc_view_positioner_get_anchor_rect(Pointer view);

	/**
	 * Get offset requested by positioner, as defined in xdg-shell v6. Returns
	 * NULL if view has no valid positioner, or default value (0, 0) if
	 * positioner has no offset set.
	 */
	wlc_point wlc_view_positioner_get_offset(Pointer view);

	/**
	 * Get anchor requested by positioner, as defined in xdg-shell v6. Returns
	 * default value WLC_BIT_ANCHOR_NONE if view has no valid positioner or if
	 * positioner has no anchor set.
	 */
	int wlc_view_positioner_get_anchor(Pointer view);

	/**
	 * Get anchor requested by positioner, as defined in xdg-shell v6. Returns
	 * default value WLC_BIT_GRAVITY_NONE if view has no valid positioner or if
	 * positioner has no gravity set.
	 */
	int wlc_view_positioner_get_gravity(Pointer view);

	/**
	 * Get constraint adjustment requested by positioner, as defined in
	 * xdg-shell v6. Returns default value WLC_BIT_CONSTRAINT_ADJUSTMENT_NONE if
	 * view has no valid positioner or if positioner has no constraint
	 * adjustment set.
	 */
	int wlc_view_positioner_get_constraint_adjustment(Pointer view);

	/** Get visible geometry. (what wlc displays) */
	void wlc_view_get_visible_geometry(Pointer view, wlc_geometry out_geometry);

	/**
	 * Set geometry. Set edges if the geometry change is caused by interactive
	 * resize.
	 */
	void wlc_view_set_geometry(Pointer view, int edges, wlc_geometry g);

	/** Get type bitfield. */
	int wlc_view_get_type(Pointer view);

	/** Set type bit. Toggle indicates whether it is set or not. */
	void wlc_view_set_type(Pointer view, int type, boolean toggle);

	/** Get current state bitfield. */
	int wlc_view_get_state(Pointer view);

	/** Set state bit. Toggle indicates whether it is set or not. */
	void wlc_view_set_state(Pointer view, int state, boolean toggle);

	/** Get parent view. */
	Pointer wlc_view_get_parent(Pointer view);

	/** Set parent view. */
	void wlc_view_set_parent(Pointer view, Pointer parent);

	/** Get title. */
	String wlc_view_get_title(Pointer view);

	/** Get instance. (shell-surface only) */
	String wlc_view_get_instance(Pointer view);

	/** Get class. (shell-surface only) */
	String wlc_view_get_class(Pointer view);

	/** Get app id. (xdg-surface only) */
	String wlc_view_get_app_id(Pointer view);

	/** Get pid. */
	int wlc_view_get_pid(Pointer view);

	/**
	 * -- Input API Very recent stuff, things may change. XXX: This api is dumb
	 * and assumes there is only single xkb state and keymap. In case of
	 * multiple keyboards, we want to each keyboard have own state and layout.
	 * Thus we need wlc_handle for keyboards eventually.
	 */

	/**
	 * Internal xkb_state exposed. You can use it to do more advanced key
	 * handling. However you should avoid messing up with its state.
	 */
	Pointer wlc_keyboard_get_xkb_state();

	/**
	 * Internal xkb_keymap exposed. You can use it to do more advanced key
	 * handling.
	 */
	Pointer wlc_keyboard_get_xkb_keymap();

	/** Get currently held keys. */
	Pointer wlc_keyboard_get_current_key(IntByReference ref);

	/**
	 * Utility function to convert raw keycode to keysym. Passed modifiers may
	 * transform the key.
	 */
	int wlc_keyboard_get_keysym_for_key(int key, wlc_modifiers modifiers);

	/**
	 * Utility function to convert raw keycode to Unicode/UTF-32 codepoint.
	 * Passed modifiers may transform the key.
	 */
	int wlc_keyboard_get_utf32_for_key(int key, wlc_modifiers mods);

	/** Get current pointer position. */
	void wlc_pointer_get_position(wlc_point position);

	/** Set current pointer position. */
	void wlc_pointer_set_position(wlc_point position);
	
	// wlc-wayland.h
	
	/** Returns Wayland display. */
	Pointer wlc_get_wl_display();

	/** Returns view handle from wl_surface resource. */
	Pointer wlc_handle_from_wl_surface_resource(Pointer resource);

	/** Returns output handle from wl_output resource. */
	Pointer wlc_handle_from_wl_output_resource(Pointer resource);

	/** Returns internal wlc surface from wl_surface resource. */
	Pointer wlc_resource_from_wl_surface_resource(Pointer resource);

	/** Get surface size. */
	wlc_size wlc_surface_get_size(Pointer surface); 

	/** Return wl_surface resource from internal wlc surface. */
	Pointer wlc_surface_get_wl_resource(Pointer surface);

	/**
	 * Turns wl_surface into a wlc view. Returns 0 on failure. This will also trigger view.created callback as any view would.
	 * For the extra arguments see details of wl_resource_create and wl_resource_set_implementation.
	 * The extra arguments may be set NULL, if you are not implementing Wayland interface for the surface role.
	 */
	Pointer wlc_view_from_surface(Pointer surface, Pointer wlClient, Pointer wlInterface, Pointer implementation, int version, int id, Pointer userdata);

	/** Returns internal wlc surface from view handle */
	Pointer wlc_view_get_surface(Pointer view);

	/** Returns a list of the subsurfaces of the given surface */
	Pointer wlc_surface_get_subsurfaces(Pointer parent, IntByReference ref);

	/** Returns the size of a subsurface and its position relative to parent */
	void wlc_get_subsurface_geometry(Pointer surface, wlc_geometry out_geometry);

	/** Returns wl_client from view handle */
	Pointer wlc_view_get_wl_client(Pointer view);

	/** Returns surface role resource from view handle. Return value will be NULL if the view was not assigned role or created with wlc_view_create_from_surface(). */
	Pointer wlc_view_get_role(Pointer view);
	
	// wlc-render.h
	
//	/**
//	 * Write pixel data with the specific format to output's framebuffer.
//	 * If the geometry is out of bounds, it will be automaticall clamped.
//	 */
//	WLC_NONULL void wlc_pixels_write(enum wlc_pixel_format format, const struct wlc_geometry *geometry, const void *data);
//
//	/**
//	 * Read pixel data from output's framebuffer.
//	 * If the geometry is out of bounds, it will be automatically clamped.
//	 * Potentially clamped geometry will be stored in out_geometry, to indicate width / height of the returned data.
//	 */
//	WLC_NONULL void wlc_pixels_read(enum wlc_pixel_format format, const struct wlc_geometry *geometry, struct wlc_geometry *out_geometry, void *out_data);
//
//	/** Renders surface. */
//	WLC_NONULL void wlc_surface_render(wlc_resource surface, const struct wlc_geometry *geometry);
//
//	/**
//	 * Schedules output for rendering next frame. If output was already scheduled this is no-op,
//	 * if output is currently rendering, it will render immediately after.
//	 */
//	void wlc_output_schedule_render(wlc_handle output);
//
//	/**
//	 * Adds frame callbacks of the given surface for the next output frame.
//	 * It applies recursively to all subsurfaces.
//	 * Useful when the compositor creates custom animations which require disabling internal rendering,
//	 * but still need to update the surface textures (for ex. video players).
//	 */
//	void wlc_surface_flush_frame_callbacks(wlc_resource surface);
//	
//
//	/** Returns currently active renderer on the given output */
//	enum wlc_renderer wlc_output_get_renderer(wlc_handle output);
//	
//	/**
//	 * Fills out_textures[] with the textures of a surface. Returns false if surface is invalid.
//	 * Array must have at least 3 elements and should be refreshed at each frame.
//	 * Note that these are not only OpenGL textures but rather render-specific.
//	 * For more info what they are check the renderer's source code */
//	bool wlc_surface_get_textures(wlc_resource surface, uint32_t out_textures[3], enum wlc_surface_format *out_format);
	
}
