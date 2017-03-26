package cz.upol.inf.vanusanik.jwlc.wayland.impl;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.wayland.WaylandClient;
import cz.upol.inf.vanusanik.jwlc.wayland.WaylandClientFactory;

public class SimpleWaylandFactory implements WaylandClientFactory {

	public WaylandClient create(Pointer p) {
		return new SimpleWaylandClient(p);
	}

}
