package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import cz.upol.inf.vanusanik.jwlc.wlc.LogType;

public interface LoggerCallback {
	
	public void onLog(LogType type, String message);

}
