package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.wlc.View;

public interface PointerMotionCallback {
	
	public boolean onPointerMotion(View view, long time, Point position);

}
