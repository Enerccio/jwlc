package cz.upol.inf.vanusanik.jwlc.wayland;

import com.sun.jna.Pointer;

public interface WaylandClientFactory {

	WaylandClient create(Pointer p);
	
}
