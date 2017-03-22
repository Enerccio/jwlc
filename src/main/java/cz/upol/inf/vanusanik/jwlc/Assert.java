package cz.upol.inf.vanusanik.jwlc;

public class Assert {
	
	public static void assertNotNull(Object o) {
		if (o == null)
			throw new NullPointerException();
	}

}
