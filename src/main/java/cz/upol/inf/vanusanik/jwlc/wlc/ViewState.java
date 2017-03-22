package cz.upol.inf.vanusanik.jwlc.wlc;

public interface ViewState {

	public static final int MAXIMIZED = 1 << 0;
	public static final int FULLSCREEN = 1 << 1;
	public static final int RESIZING = 1 << 2;
	public static final int MOVING = 1 << 3;
	public static final int ACTIVATED = 1 << 4;

}
