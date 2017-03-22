package cz.upol.inf.vanusanik.jwlc.wlc;

public interface Modifier {
	public static final int SHIFT = 1 << 0;
	public static final int CAPS = 1 << 1;
	public static final int CTRL = 1 << 2;
	public static final int ALT = 1 << 3;
	public static final int MOD2 = 1 << 4;
	public static final int MOD3 = 1 << 5;
	public static final int LOGO = 1 << 6;
	public static final int MOD5 = 1 << 7;
}
