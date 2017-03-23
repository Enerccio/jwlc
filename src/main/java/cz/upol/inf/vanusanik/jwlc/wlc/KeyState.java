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

	public static KeyState from(int keyState) {
		for (KeyState ks : values())
			if (ks.ni == keyState)
				return ks;
		return null;
	}

}
