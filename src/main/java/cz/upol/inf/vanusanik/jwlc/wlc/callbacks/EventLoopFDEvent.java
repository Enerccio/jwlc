package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import java.io.FileDescriptor;

public interface EventLoopFDEvent {

	public int onFDAvailable(FileDescriptor fd, long mask, Object data);
	
}
