package cz.upol.inf.vanusanik.jwlc.wlc;

public interface PositionerConstraintAdjustment {
	public static final int NONE = 0;
	public static final int SLIDE_X = 1 << 0;
	public static final int SLIDE_Y = 1 << 1;
	public static final int FLIP_X = 1 << 2;
	public static final int FLIP_Y = 1 << 3;
	public static final int RESIZE_X = 1 << 4;
	public static final int RESIZE_Y = 1 << 5;
}
