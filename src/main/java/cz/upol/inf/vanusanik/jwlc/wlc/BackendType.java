package cz.upol.inf.vanusanik.jwlc.wlc;

public enum BackendType {

	NONE(0), DRM(1), X11(2)

	;

	private int ni;

	BackendType(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

}
