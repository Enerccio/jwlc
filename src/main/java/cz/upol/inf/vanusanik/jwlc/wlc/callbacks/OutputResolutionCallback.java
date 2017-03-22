package cz.upol.inf.vanusanik.jwlc.wlc.callbacks;

import cz.upol.inf.vanusanik.jwlc.geometry.Size;
import cz.upol.inf.vanusanik.jwlc.wlc.Output;

public interface OutputResolutionCallback {

	public void onOutputResolution(Output handle, Size fromSize, Size toSize);
	
}
