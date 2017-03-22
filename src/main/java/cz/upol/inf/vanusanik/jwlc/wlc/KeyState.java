package cz.upol.inf.vanusanik.jwlc.wlc;

public enum KeyState {

	STATE_RELEASED(0), STATE_PRESSED(1)

	;

	private int ni;

	KeyState(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

}
