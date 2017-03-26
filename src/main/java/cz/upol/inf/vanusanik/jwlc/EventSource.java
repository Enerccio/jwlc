package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Pointer;

public class EventSource {

	private final Pointer handle;

	protected EventSource(Pointer handle) {
		this.handle = handle;
	}

	public static EventSource from(Pointer handle) {
		if (handle == null)
			return null;
		return new EventSource(handle);
	}

	public Pointer to() {
		return handle;
	}

	public Pointer getHandle() {
		return handle;
	}

}
