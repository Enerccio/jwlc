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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Size;
import cz.upol.inf.vanusanik.jwlc.render.SurfaceFormat;
import cz.upol.inf.vanusanik.jwlc.wayland.WaylandClient;
import cz.upol.inf.vanusanik.jwlc.wayland.WaylandInterface;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.WLCHandle;

public class Resource implements PointerContainer {
	
	public static class Textures {
		private final long[] textures;
		private final SurfaceFormat format;
		
		public Textures(long[] textures, SurfaceFormat format) {
			this.textures = textures;
			this.format = format;
		}
		
		public long[] getTextures() {
			return textures;
		}
		public SurfaceFormat getFormat() {
			return format;
		}

		@Override
		public String toString() {
			return "Textures [textures=" + Arrays.toString(textures)
					+ ", format=" + format + "]";
		}	
		
	}
	
	private final Pointer handle;

	protected Resource(Pointer handle) {
		this.handle = handle;
	}

	public static Resource from(Pointer handle) {
		if (handle == null)
			return null;
		return new Resource(handle);
	}

	public Pointer to() {
		return handle;
	}

	public Pointer getHandle() {
		return handle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Resource [handle=" + handle + "]";
	}

	/* Methods */
	/* Getters */
	
	public Resource getInternalWLCSurface() {
		Assert.assertNotNull(this);
		Assert.assertNotNull(this.handle);
	
		return Resource.from(JWLC.nativeHandler().wlc_resource_from_wl_surface_resource(to()));
	}
	
	public Size getSurfaceSize() {
		return Size.from(JWLC.nativeHandler().wlc_surface_get_size(this.to()));
	}
	
	public List<Resource> getSubsurfaces() {
		List<Resource> rList = new ArrayList<Resource>();
		IntByReference ref = new IntByReference();
		
		Pointer data = JWLC.nativeHandler().wlc_surface_get_subsurfaces(this.to(), ref);
		for (int i=0; i<ref.getValue(); i++)
			rList.add(from(data.getPointer(i * Pointer.SIZE)));
		
		return rList;
	}
	
	public Geometry getRelativeGeometry() {
		wlc_geometry g = new wlc_geometry();
		JWLC.nativeHandler().wlc_get_subsurface_geometry(this.to(), g);
		return Geometry.from(g);
	}
	
	public Textures getTextures() {
		return getTextures(3);
	}
	
	public Textures getTextures(int count) {
		if (count < 3)
			throw new IllegalArgumentException("number of textures must be at least 3");
		int[] textures = new int[count];
		IntByReference format = new IntByReference();
		if (!JWLC.nativeHandler().wlc_surface_get_texture(this.to(), textures, format))
			return null;
		SurfaceFormat fmt = SurfaceFormat.from(format.getValue());
		long[] data = new long[count];
		for (int i=0; i<count; i++)
			data[i] = Utils.getUnsignedInt(textures[i]);
		return new Textures(data, fmt);
	}
	
	/* Setters */
	
	/* Functionality */
	
	public WLCHandle convertFromSufrace() {
		Assert.assertNotNull(this);
		Assert.assertNotNull(this.handle);
		
		return WLCHandle.from(JWLC.nativeHandler().wlc_handle_from_wl_surface_resource(to()));
	}
	
	public WLCHandle convertFromOutput() {
		Assert.assertNotNull(this);
		Assert.assertNotNull(this.handle);
	
		return WLCHandle.from(JWLC.nativeHandler().wlc_handle_from_wl_output_resource(to()));
	}
	
	public Resource convertInternalWLCSurface() {
		Assert.assertNotNull(this);
		Assert.assertNotNull(this.handle);
		
		return Resource.from(JWLC.nativeHandler().wlc_surface_get_wl_resource(to()));
	}
	
	public View convertToView(WaylandClient client, WaylandInterface wayInterface, 
			PointerContainer implementation,  long version, long id, PointerContainer userdata) {
		View v = View.from(JWLC.nativeHandler().wlc_view_from_surface(to(), 
				client.to(), wayInterface.to(), implementation.to(), 
				Utils.getAsUnsignedInt(version), Utils.getAsUnsignedInt(id), 
				userdata.to()));
		if (v.equals(View.INVALID_VIEW))
			return null;
		return v;
	}
	
	public void flushFrameCallbacks() {
		JWLC.nativeHandler().wlc_surface_flush_frame_callbacks(this.to());
	}
}
