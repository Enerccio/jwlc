package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.wlc.View;

public interface ViewRequestMoveCallback {

	public void onRequestMove(View view, Point point);
	
}
