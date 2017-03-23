package cz.upol.inf.vanusanik.jwlc.wlc;

import com.sun.jna.Pointer;

public class WLCHandle {
	
	private final Pointer handle;
	
	protected WLCHandle(Pointer handle) {
		this.handle = handle;
	}
	
	public static WLCHandle from(Pointer handle) {
		if (handle == null)
			return null;
		return new WLCHandle(handle);
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
		WLCHandle other = (WLCHandle) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WLCHandle [handle=" + handle + "]";
	}
	
}
