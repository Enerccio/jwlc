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
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputContextCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputContextDestroyedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputDestroyedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputFocusCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputPostRenderCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputPreRenderCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputResolutionCallback;

public class Output extends WLCHandle {
	
	public static final Output INVALID_OUTPUT = new Output(null);

	protected Output(Pointer handle) {
		super(handle);
	}
	
	@Override
	public String toString() {
		return "Output [getHandle()=" + getHandle() + "]";
	}
	
	public static Output from(Pointer handle) {
		if (handle == null)
			return null;
		return new Output(handle);
	}
	
	public static void setCreatedCallback(final OutputCreatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_created_cb(new handle_callback() {

			public boolean callback(Pointer handle) {
				return cb.onOutputCreated(Output.from(handle));
			}
		});
	}
	
	public static void setDestroyedCallback(final OutputDestroyedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_destroyed_cb(new handle_callback_void() {

			public void callback(Pointer handle) {
				cb.onOutputDestroyed(Output.from(handle));
			}
		});
	}
	
	public static void setFocusCallback(final OutputFocusCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_focus_cb(new focus_callback() {

			public void callback(Pointer handle, boolean focusState) {
				cb.onFocusChange(Output.from(handle), focusState);
			}
		});
	}

	public static void setResolutionCallback(final OutputResolutionCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_resolution_cb(new output_resolution_callback() {

			public void callback(Pointer handle, wlc_size fromSize, wlc_size toSize) {
				cb.onOutputResolution(Output.from(handle), Size.from(fromSize),
						Size.from(toSize));
			}
		});
	}
	
	public static void setPreRenderCallback(final OutputPreRenderCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_render_pre_cb(new handle_callback_void() {

			public void callback(Pointer handle) {
				cb.onOutputPreRender(Output.from(handle));
			}
		});
	}
	
	public static void setPostRenderCallback(final OutputPostRenderCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_render_post_cb(new handle_callback_void() {

			public void callback(Pointer handle) {
				cb.onOutputPostRender(Output.from(handle));
			}
		});
	}
	
	public static void setContextCreatedCallback(final OutputContextCreatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_context_created_cb(new handle_callback_void() {

			public void callback(Pointer handle) {
				cb.onOutputContextCreated(Output.from(handle));
			}
		});
	}
	
	public static void setContextDestroyedCallback(final OutputContextDestroyedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_context_destroyed_cb(new handle_callback_void() {

			public void callback(Pointer handle) {
				cb.onOutputContextDestroyed(Output.from(handle));
			}
		});
	}
	
	public Size getVirtualResolution() {
		return Size.from(JWLC.nativeHandler().wlc_output_get_virtual_resolution(this.to()));
	}
	
	public List<View> getViews() {
		List<View> data = new ArrayList<View>();

		IntByReference memb = new IntByReference();
		Pointer p = JWLC.nativeHandler().wlc_output_get_views(this.to(), memb);
		long n = Utils.getUnsignedInt(memb.getValue());
		for (long i = 0; i < n; i++)
			data.add(View.from(p.getPointer(i*Pointer.SIZE)));

		return data;
	}
	
	public long getMask() {
		return Utils.getUnsignedInt(JWLC.nativeHandler().wlc_output_get_mask(this.to()));
	}
	
	public static List<Output> getOutputs() {
		List<Output> data = new ArrayList<Output>();

		IntByReference memb = new IntByReference();
		Pointer p = JWLC.nativeHandler().wlc_get_outputs(memb);
		long n = Utils.getUnsignedInt(memb.getValue());
		for (long i = 0; i < n; i++)
			data.add(Output.from(p.getPointer(i*Pointer.SIZE)));

		return data;
	}
	
	public void setResolution(Size resolution, long scale) {
		Assert.assertNotNull(resolution);
		
		JWLC.nativeHandler().wlc_output_set_resolution(this.to(), 
				resolution.to(), Utils.getAsUnsignedInt(scale));
	}

	public Size getResolution() {
		return Size.from(JWLC.nativeHandler().wlc_output_get_resolution(this.to()));
	}
}
