package cz.upol.inf.vanusanik.jwlc.wlc;

public enum ButtonState {

	STATE_RELEASED(0), STATE_PRESSED(1)

	;

	private int ni;

	ButtonState(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

	public static ButtonState from(int buttonState) {
		for (ButtonState bs : values())
			if (bs.ni == buttonState)
				return bs;
		return null;
	}

}
