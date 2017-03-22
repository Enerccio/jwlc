package cz.upol.inf.vanusanik.jwlc.wlc;

public enum TouchType {

	DOWN(0), UP(1), MOTION(2), FRAME(3), CANCEL(4)

	;

	private int ni;

	TouchType(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

}
