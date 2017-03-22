package cz.upol.inf.vanusanik.jwlc.wlc;

public interface ViewType {

	public static final int OVERRIDE_REDIRECT = 1 << 0; // Override redirect
														// (x11)
	public static final int UNMANAGED = 1 << 1; // Tooltips, DnD's, menus
												// (x11)
	public static final int SPLASH = 1 << 2; // Splash screens (x11)
	public static final int MODAL = 1 << 3; // Modal windows (x11)
	public static final int POPUP = 1 << 4; // xdg-shell, wl-shell popups

}
