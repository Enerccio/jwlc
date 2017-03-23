package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import cz.upol.inf.vanusanik.jwlc.wlc.KeyState;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.View;

public interface KeyboardCallback {
	
	public boolean onKeyboard(View view, long time, Modifiers modifiers, long key, KeyState state);

}
