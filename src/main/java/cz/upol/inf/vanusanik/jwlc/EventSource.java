package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Pointer;

public class EventSource {
	
	private final Pointer handle;
	private final long dataPointer;

	protected EventSource(Pointer handle, long dataPointer) {
		this.handle = handle;
		this.dataPointer = dataPointer;
	}

	public static EventSource from(Pointer handle, long dataPointer) {
		if (handle == null)
			return null;
		return new EventSource(handle, dataPointer);
	}

	public Pointer to() {
		return handle;
	}

	public Pointer getHandle() {
		return handle;
	}

	public long getDataPointer() {
		return dataPointer;
	}

}
