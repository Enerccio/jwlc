package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.wlc.ButtonState;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.View;

public interface PointerButtonCallback {

	public boolean onPointerButton(View view, long time, Modifiers mods, long button, ButtonState state, Point position);
	
}
