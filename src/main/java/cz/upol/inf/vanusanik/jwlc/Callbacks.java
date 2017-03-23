package cz.upol.inf.vanusanik.jwlc;

import com.sun.jna.Callback;

import cz.upol.inf.vanusanik.jwlc.geometry.Geometry.wlc_geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Point.wlc_point;
import cz.upol.inf.vanusanik.jwlc.geometry.Size.wlc_size;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers.wlc_modifiers;

public class Callbacks {

	public interface handle_callback extends Callback {
		boolean callback(int handle);
	}
	
	public interface handle_callback_void extends Callback {
		void callback(int handle);
	}
	
	public interface focus_callback extends Callback {
		void callback(int handle, boolean focus);
	}
	
	public interface logger_callback extends Callback {
		void callback(int type, String message);
	}
	
	public interface output_resolution_callback extends Callback {
		void callback(int handle, wlc_size fromSize, wlc_size toSize);
	}
	
	public interface request_move_callback extends Callback {
		void callback(int handle, wlc_point point);
	}
	
	public interface request_resize_callback extends Callback {
		void callback(int handle, int edges, wlc_point point);
	}
	
	public interface geometry_callback extends Callback {
		void callback(int handle, wlc_geometry g);
	}
	
	public interface keyboard_callback extends Callback {
		boolean callback(int handle, int time, wlc_modifiers mods, int key, int keyState);
	}
	
	public interface pointer_button_callback extends Callback {
		boolean callback(int handle, int time, wlc_modifiers mods, int button, int buttonState, wlc_point point);
	}
}
