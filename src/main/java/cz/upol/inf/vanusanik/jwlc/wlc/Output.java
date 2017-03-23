package cz.upol.inf.vanusanik.jwlc.wlc;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import cz.upol.inf.vanusanik.jwlc.Assert;
import cz.upol.inf.vanusanik.jwlc.Utils;
import cz.upol.inf.vanusanik.jwlc.Callbacks.handle_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.output_resolution_callback;
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.geometry.Size;
import cz.upol.inf.vanusanik.jwlc.geometry.Size.wlc_size;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputResolutionCallback;

public class Output extends WLCHandle {
	
	public static final Output INVALID_OUTPUT = new Output(0);

	protected Output(long handle) {
		super(handle);
	}
	
	@Override
	public String toString() {
		return "Output [getHandle()=" + getHandle() + "]";
	}
	
	public static Output from(long handle) {
		if (handle == 0)
			return null;
		return new Output(handle);
	}
	
	public static void setCreatedCallback(final OutputCreatedCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_created_cb(new handle_callback() {

			public boolean callback(int handle) {
				return cb.onOutputCreated(Output.from(Utils.getUnsignedInt(handle)));
			}
		});
	}

	public static void setResolutionCallback(final OutputResolutionCallback cb) {
		Assert.assertNotNull(cb);

		JWLC.nativeHandler().wlc_set_output_resolution_cb(new output_resolution_callback() {

			public void callback(int handle, wlc_size fromSize, wlc_size toSize) {
				cb.onOutputResolution(Output.from(Utils.getUnsignedInt(handle)), Size.from(fromSize),
						Size.from(toSize));
			}
		});
	}
	
	public Size getVirtualResolution() {
		return Size.from(JWLC.nativeHandler().wlc_output_get_virtual_resolution(
				Utils.getAsUnsignedInt(this.to())));
	}
	
	public List<View> getViews() {
		List<View> data = new ArrayList<View>();

		IntByReference memb = new IntByReference();
		Pointer p = JWLC.nativeHandler().wlc_output_get_views(Utils.getAsUnsignedInt(this.to()), memb);
		long n = Utils.getUnsignedInt(memb.getValue());
		for (long i = 0; i < n; i++)
			data.add(View.from(Utils.getUnsignedInt(p.getInt(i*4))));

		return data;
	}
	
	public long getMask() {
		return Utils.getUnsignedInt(JWLC.nativeHandler().wlc_output_get_mask(Utils.getAsUnsignedInt(this.to())));
	}
	
	public static List<Output> getOutputs() {
		List<Output> data = new ArrayList<Output>();

		IntByReference memb = new IntByReference();
		Pointer p = JWLC.nativeHandler().wlc_get_outputs(memb);
		long n = Utils.getUnsignedInt(memb.getValue());
		for (long i = 0; i < n; i++)
			data.add(Output.from(Utils.getUnsignedInt(p.getInt(i*4))));

		return data;
	}
	
	public void setResolution(Size resolution, long scale) {
		Assert.assertNotNull(resolution);
		
		JWLC.nativeHandler().wlc_output_set_resolution(Utils.getAsUnsignedInt(this.to()), 
				resolution.to(), Utils.getAsUnsignedInt(scale));
	}

	public Size getResolution() {
		return Size.from(JWLC.nativeHandler().wlc_output_get_resolution(Utils.getAsUnsignedInt(this.to())));
	}
}
