package cz.upol.inf.vanusanik.jwlc.wlc;

public class WLCHandle {
	
	private final long handle;
	
	protected WLCHandle(long handle) {
		this.handle = handle;
	}
	
	public static WLCHandle from(long handle) {
		if (handle == 0)
			return null;
		return new WLCHandle(handle);
	}
	
	public long to() {
		return handle;
	}

	public long getHandle() {
		return handle;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (handle ^ (handle >>> 32));
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
		if (handle != other.handle)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WLCHandle [handle=" + handle + "]";
	}
	
}
