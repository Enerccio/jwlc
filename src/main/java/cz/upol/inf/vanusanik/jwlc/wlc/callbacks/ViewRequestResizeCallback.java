package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.wlc.View;

public interface ViewRequestResizeCallback {

	public void onRequestResize(View view, long edges, Point origin);
	
}
