package cz.upol.inf.vanusanik.jwlc.wlc;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Size;

public class ViewPositioner {

	private View handler;
	
	ViewPositioner(View handler) {
		this.handler = handler;
	}
	
	public Geometry getAnchorRect() {
		return Geometry.from(JWLC.nativeHandler().wlc_view_positioner_get_anchor_rect(handler.to()));
	}
	
	public Size getSize() {		
		return Size.from(JWLC.nativeHandler().wlc_view_positioner_get_size(handler.to()));
	}
	
}
