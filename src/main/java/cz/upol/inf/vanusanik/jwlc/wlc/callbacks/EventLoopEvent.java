package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import cz.upol.inf.vanusanik.jwlc.EventSource;

public interface EventLoopEvent {
	
	public int onEvent(EventSource event, Object data);

}
