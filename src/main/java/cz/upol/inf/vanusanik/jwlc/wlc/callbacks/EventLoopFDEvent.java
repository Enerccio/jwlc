package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import java.io.FileDescriptor;

import cz.upol.inf.vanusanik.jwlc.EventSource;

public interface EventLoopFDEvent {

	public int onFDAvailable(EventSource event, FileDescriptor fd, long mask);

}
