package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import java.io.FileDescriptor;

import cz.upol.inf.vanusanik.jwlc.Event;

public interface EventLoopFDEvent {

	public int onFDAvailable(Event event, FileDescriptor fd, long mask);

}
