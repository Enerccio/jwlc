package cz.upol.inf.vanusanik.jwlc.wayland.impl;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.wayland.WaylandClient;

public class SimpleWaylandClient implements WaylandClient {
	
	private final Pointer p;
	
	SimpleWaylandClient(Pointer p) {
		this.p = p;
	}

	public Pointer to() {
		return p;
	}

	public Pointer getHandle() {
		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleWaylandClient other = (SimpleWaylandClient) obj;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SimpleWaylandClient [p=" + p + "]";
	}
	
}
