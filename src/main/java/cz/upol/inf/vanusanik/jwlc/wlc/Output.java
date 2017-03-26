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

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import cz.upol.inf.vanusanik.jwlc.Assert;
import cz.upol.inf.vanusanik.jwlc.Callbacks.focus_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_callback_void;
import cz.upol.inf.vanusanik.jwlc.Callbacks.output_resolution_callback;
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Utils;
import cz.upol.inf.vanusanik.jwlc.geometry.Size;
import cz.upol.inf.vanusanik.jwlc.geometry.Size.wlc_size;
import cz.upol.inf.vanusanik.jwlc.render.Renderer;
import cz.upol.inf.vanusanik.jwlc.utils.MutableViewList;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputContextCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputContextDestroyedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputDestroyedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputFocusCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputPostRenderCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputPreRenderCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputResolutionCallback;

/**
 * wlc_output abstraction wrapper class.
 * @author enerccio
 *
 */
public class Output extends WLCHandle {

	/**
	 * Invalid output, where handle pointer is 0.
	 */
	public static final Output INVALID_OUTPUT = new Output(new Pointer(0));

	protected Output(Pointer handle) {
		super(handle);
	}

	@Override
	public String toString() {
		return "Output [getHandle()=" + getHandle() + "]";
	}

	/**
	 * Creates output from provided pointer.
	 * 
	 * Internal method, use with care.
	 * @param handle
	 * @return
	 */
	public static Output from(Pointer handle) {
		if (handle == null)
			return null;
		return new Output(handle);
	}

	/* Callbacks */

	/**
	 * Sets callback to be called when output is created.
	 * @param cb
	 */
	public static void setCreatedCallback(final OutputCreatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_created_cb(new handle_callback() {

			public boolean callback(Pointer handle) {
				return cb.onCreated(Output.from(handle));
			}
		});
	}

	/**
	 * Sets callback to be called when output is destroyed.
	 * @param cb
	 */
	public static void setDestroyedCallback(final OutputDestroyedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_output_destroyed_cb(new handle_callback_void() {

					public void callback(Pointer handle) {
						cb.onDestroyed(Output.from(handle));
					}
				});
	}

	/**
	 * Sets callback to be called when output focus is changed.
	 * @param cb
	 */
	public static void setFocusCallback(final OutputFocusCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_focus_cb(new focus_callback() {

			public void callback(Pointer handle, boolean focusState) {
				cb.onFocusChange(Output.from(handle), focusState);
			}
		});
	}

	/**
	 * Sets callback when resolution of this output is changed.
	 * @param cb
	 */
	public static void setResolutionCallback(
			final OutputResolutionCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_output_resolution_cb(new output_resolution_callback() {

					public void callback(Pointer handle, wlc_size fromSize,
							wlc_size toSize) {
						cb.onResolution(Output.from(handle),
								Size.from(fromSize), Size.from(toSize));
					}
				});
	}

	/**
	 * Sets callback which is called when output is about to be rendered.
	 * @param cb
	 */
	public static void setPreRenderCallback(final OutputPreRenderCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_output_render_pre_cb(new handle_callback_void() {

					public void callback(Pointer handle) {
						cb.onPreRender(Output.from(handle));
					}
				});
	}

	/**
	 * Sets callback which is called when output was rendered.
	 * @param cb
	 */
	public static void setPostRenderCallback(
			final OutputPostRenderCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_output_render_post_cb(new handle_callback_void() {

					public void callback(Pointer handle) {
						cb.onPostRender(Output.from(handle));
					}
				});
	}

	/**
	 * Sets callback to be called when context is created for this output.
	 * @param cb
	 */
	public static void setContextCreatedCallback(
			final OutputContextCreatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler()
				.wlc_set_output_context_created_cb(new handle_callback_void() {

					public void callback(Pointer handle) {
						cb.onContextCreated(Output.from(handle));
					}
				});
	}

	/**
	 * Sets callback to be called when context is destroyed for this output.
	 * @param cb
	 */
	public static void setContextDestroyedCallback(
			final OutputContextDestroyedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_context_destroyed_cb(
				new handle_callback_void() {

					public void callback(Pointer handle) {
						cb.onContextDestroyed(Output.from(handle));
					}
				});
	}

	/* Methods */
	/* Getters */

	/**
	 * @return name of this output.
	 */
	public String getName() {
		return JWLC.nativeHandler().wlc_output_get_name(this.to());
	}

	/**
	 * @return whether this output is asleep or not
	 */
	public boolean isAsleep() {
		return JWLC.nativeHandler().wlc_output_get_sleep(this.to());
	}

	/**
	 * @return gamma size 
	 */
	public int getGammaSize() {
		return Utils.getUnsignedShort(
				JWLC.nativeHandler().wlc_output_get_gamma_size(this.to()));
	}

	/**
	 * @return virtual resolution of this output
	 */
	public Size getVirtualResolution() {
		return Size.from(JWLC.nativeHandler()
				.wlc_output_get_virtual_resolution(this.to()));
	}

	/**
	 * @return list of views for this output
	 */
	public List<View> getViews() {
		List<View> data = new ArrayList<View>();

		IntByReference memb = new IntByReference();
		Pointer p = JWLC.nativeHandler().wlc_output_get_views(this.to(), memb);
		long n = Utils.getUnsignedInt(memb.getValue());
		for (long i = 0; i < n; i++)
			data.add(View.from(p.getPointer(i * Pointer.SIZE)));

		return data;
	}

	/**
	 * @return mask
	 */
	public long getMask() {
		return Utils.getUnsignedInt(
				JWLC.nativeHandler().wlc_output_get_mask(this.to()));
	}

	/**
	 * @return all outputs currently active
	 */
	public static List<Output> getOutputs() {
		List<Output> data = new ArrayList<Output>();

		IntByReference memb = new IntByReference();
		Pointer p = JWLC.nativeHandler().wlc_get_outputs(memb);
		long n = Utils.getUnsignedInt(memb.getValue());
		for (long i = 0; i < n; i++)
			data.add(Output.from(p.getPointer(i * Pointer.SIZE)));

		return data;
	}

	/**
	 * Get real resolution. 
	 * Resolution applied by either {@link #setResolution(Size, long)} call or initially. 
	 * Do not use this for coordinate boundary.
	 * 
	 * @return real resolution of this output.
	 */
	public Size getResolution() {
		return Size.from(
				JWLC.nativeHandler().wlc_output_get_resolution(this.to()));
	}

	/**
	 * @return currently focused output.
	 */
	public static Output getFocused() {
		return Output.from(JWLC.nativeHandler().wlc_get_focused_output());
	}

	/**
	 * @return scale factor
	 */
	public long getScale() {
		return Utils.getUnsignedInt(
				JWLC.nativeHandler().wlc_output_get_scale(this.to()));
	}

	/**
	 * Returns mutable view list of all views of this output.
	 * 
	 * This is mainly useful for wm's who need another view stack for inplace sorting. 
	 * For example tiling wms, may want to use this to keep their tiling order 
	 * separated from floating order.
	 * 
	 * @return mutable view.
	 * @see {@link MutableViewList} on how to correctly use this object
	 */
	public MutableViewList getMutableViews() {
		IntByReference ref = new IntByReference();
		Pointer p = JWLC.nativeHandler().wlc_output_get_mutable_views(this.to(),
				ref);
		return new MutableViewList(p, ref.getValue());
	}

	/**
	 * @return renderer for this output
	 */
	public Renderer getRenderer() {
		return Renderer
				.from(JWLC.nativeHandler().wlc_output_get_renderer(this.to()));
	}

	/* Setters */

	/**
	 * Sets the sleep status of this output.
	 */
	public void setSleep(boolean sleep) {
		JWLC.nativeHandler().wlc_output_set_sleep(this.to(), sleep);
	}

	/**
	 * Sets gamma values.
	 * @param len 
	 * @param r arrays of uint8_t color ramp values as shorts, must be len length
	 * @param g arrays of uint8_t color ramp values as shorts, must be len length
	 * @param b arrays of uint8_t color ramp values as shorts, must be len length
	 */
	public void setGamma(int len, short[] r, short[] g, short[] b) {
		Assert.assertNotNull(r);
		Assert.assertNotNull(g);
		Assert.assertNotNull(b);

		if (!(r.length == len && g.length == len && b.length == len)) {
			throw new InvalidParameterException(
					"arrays must have same length of passed in len argument");
		}

		JWLC.nativeHandler().wlc_output_set_gamma(this.to(),
				Utils.getAsUnsignedShort(len), r, g, b);
	}

	/**
	 * Sets the resolution of this output.
	 * @param resolution
	 * @param scale
	 */
	public void setResolution(Size resolution, long scale) {
		Assert.assertNotNull(resolution);

		JWLC.nativeHandler().wlc_output_set_resolution(this.to(),
				resolution.to(), Utils.getAsUnsignedInt(scale));
	}

	/**
	 * Sets mask.
	 * @param mask
	 */
	public void setMask(long mask) {
		JWLC.nativeHandler().wlc_output_set_mask(this.to(),
				Utils.getAsUnsignedInt(mask));
	}

	/**
	 * After this call, output will have only views provided in the list.
	 * @param views
	 * @return
	 */
	public boolean setViews(List<View> views) {
		Pointer[] ptrArray = new Pointer[views.size()];
		int it = 0;
		for (View v : views)
			ptrArray[it++] = v.to();
		return JWLC.nativeHandler().wlc_output_set_views(this.to(), ptrArray,
				views.size());
	}

	/* Functionality */

	/**
	 * Focuses this output.
	 */
	public void focus() {
		JWLC.nativeHandler().wlc_output_focus(this.to());
	}

	/**
	 * Unfocuses all outputs (equivalent to {@link #INVALID_OUTPUT}.{@link #focus()}).
	 */
	public static void unfocus() {
		INVALID_OUTPUT.focus();
	}

	/**
	 * Schedules render for this output. 
	 * No op, if render was already scheduled for this output.
	 */
	public void requestRepaint() {
		JWLC.nativeHandler().wlc_output_schedule_render(this.to());
	}
}
