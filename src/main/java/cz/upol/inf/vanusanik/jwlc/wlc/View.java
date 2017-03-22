package cz.upol.inf.vanusanik.jwlc.wlc;

import cz.upol.inf.vanusanik.jwlc.Assert;
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Utils;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;

public class View extends WLCHandle {

	public final ViewPositioner positioner;
	
	protected View(long handle) {
		super(handle);
		positioner = new ViewPositioner(this);
	}
	
	@Override
	public String toString() {
		return "View [getHandle()=" + getHandle() + "]";
	}
	
	public static View from(long handle) {
		if (handle == 0)
			return null;
		return new View(handle);
	}

	public void setGeometry(long edges, Geometry geo) {
		Assert.assertNotNull(geo);
		
		int e = Utils.getAsUnsignedInt(edges);
		wlc_geometry g = geo.to();
		JWLC.nativeHandler().wlc_view_set_geometry(Utils.getAsUnsignedInt(this.to()), e, g);
		geo.reset(g);
	}
	
	public Geometry getGeometry() {
		return Geometry.from(JWLC.nativeHandler().wlc_view_get_geometry(Utils.getAsUnsignedInt(this.to())));
	}

	public View getParent() {
		return View.from(Utils.getUnsignedInt(
				JWLC.nativeHandler().wlc_view_get_parent(Utils.getAsUnsignedInt(this.to()))));
	}
}
