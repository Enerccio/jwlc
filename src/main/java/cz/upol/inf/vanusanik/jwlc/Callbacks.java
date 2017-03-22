package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Callback;

import cz.upol.inf.vanusanik.jwlc.geometry.Size.wlc_size;

public class Callbacks {

	public interface handle_callback extends Callback {
		boolean callback(int handle);
	}
	
	public interface logger_callback extends Callback {
		void callback(int type, String message);
	}
	
	public interface output_resolution_callback extends Callback {
		void callback(int handle, wlc_size fromSize, wlc_size toSize);
	}
}
