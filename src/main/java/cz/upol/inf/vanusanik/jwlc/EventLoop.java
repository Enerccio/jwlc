package cz.upol.inf.vanusanik.jwlc;

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.Callbacks.fd_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopFDEvent;

public class EventLoop {

	private static int usedData = 0;
	private static Map<Integer, Object> dataMap = new HashMap<Integer, Object>();

	public static EventSource addFileDescriptorEvent(FileDescriptor fd, long mask, final EventLoopFDEvent callback,
			Object customData) {
		synchronized (EventLoop.class) {
			int dataPointer = usedData++;
			dataMap.put(dataPointer, customData);
			return EventSource.from(JWLC.nativeHandler().wlc_event_loop_add_fd(Utils.getFD(fd),
					Utils.getAsUnsignedInt(mask), new fd_callback() {

						public int callback(int fd, int mask, Pointer data) {
							Object customData = dataMap.get((int) Pointer.nativeValue(data));
							return callback.onFDAvailable(Utils.createFD(fd), Utils.getUnsignedInt(mask), customData);
						}

					}, new Pointer(dataPointer)), dataPointer);
		}
	}

}
