package cz.upol.inf.vanusanik.jwlc.render;

public enum PixelFormat {

	RGBA8888(0)

	;

	private int ni;

	PixelFormat(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

}
