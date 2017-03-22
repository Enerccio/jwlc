package cz.upol.inf.vanusanik.jwlc.wlc;

public enum LogType {

	LOG_INFO(0), LOG_WARN(1), LOG_ERROR(2), LOG_WAYLAND(3),

	;

	private int ni;

	LogType(int ni) {
		this.ni = ni;
	}

	public int to() {
		return ni;
	}

}
