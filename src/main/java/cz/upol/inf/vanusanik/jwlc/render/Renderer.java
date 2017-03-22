package cz.upol.inf.vanusanik.jwlc.render;

public enum Renderer {

	RENDERER_GLES2(0), NO_RENDERER(1)

	;

	private int ni;

	Renderer(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

}
