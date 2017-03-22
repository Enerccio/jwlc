package cz.upol.inf.vanusanik.jwlc;

public class Utils {

	public static long getUnsignedInt(int x) {
	    return x & 0x00000000ffffffffL;
	}
	
	public static int getAsUnsignedInt(long x) {
		return (int)x;
	}
	
}
