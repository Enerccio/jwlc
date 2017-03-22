package cz.upol.inf.vanusanik.jwlc.render;

public enum SurfaceFormat {

	RGB(0), RGBA(1), EGL(2), Y_UV(3), Y_U_V(4), Y_XUXV(5),

	;

	private int ni;

	SurfaceFormat(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

}
