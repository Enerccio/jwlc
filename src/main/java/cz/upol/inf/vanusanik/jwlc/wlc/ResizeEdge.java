package cz.upol.inf.vanusanik.jwlc.wlc;

public enum ResizeEdge {

	NONE(0), TOP(1), BOTTOM(2), LEFT(4), TOP_LEFT(5), BOTTOM_LEFT(6), RIGHT(8), TOP_RIGHT(9), BOTTOM_RIGHT(10)

	;

	private int ni;

	ResizeEdge(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

}
