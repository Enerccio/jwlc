package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Pointer;

public class EventSource {
	
	private final Pointer handle;
	private final int dataPointer;

	protected EventSource(Pointer handle, int dataPointer) {
		this.handle = handle;
		this.dataPointer = dataPointer;
	}

	public static EventSource from(Pointer handle, int dataPointer) {
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

	public int getDataPointer() {
		return dataPointer;
	}

}
